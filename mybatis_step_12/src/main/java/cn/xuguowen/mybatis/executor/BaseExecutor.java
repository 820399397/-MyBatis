package cn.xuguowen.mybatis.executor;

import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.ResultHandler;
import cn.xuguowen.mybatis.session.RowBounds;
import cn.xuguowen.mybatis.transaction.Transaction;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * ClassName: BaseExecutor
 * Package: cn.xuguowen.mybatis.executor
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:34
 * @Version 1.0
 */
public abstract class BaseExecutor implements Executor {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(BaseExecutor.class);

    protected Configuration configuration;
    protected Transaction transaction;
    protected Executor wrapper;

    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
    }

    public BaseExecutor(Configuration configuration, Transaction transaction, Executor wrapper) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = wrapper;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
    }


    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        return doUpdate(ms, parameter);
    }

    protected abstract int doUpdate(MappedStatement ms, Object parameter) throws SQLException;

    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter,RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;

    @Override
    public Transaction getTransaction() {
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed) {
            throw new RuntimeException("Cannot commit, transaction is already closed");
        }
        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed) {
            if (required) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            logger.warn("Unexpected exception on closing transaction.  Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }

    protected void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ignore) {
            }
        }
    }


}
