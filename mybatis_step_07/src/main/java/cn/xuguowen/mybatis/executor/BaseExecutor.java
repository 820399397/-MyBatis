package cn.xuguowen.mybatis.executor;

import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.ResultHandler;
import cn.xuguowen.mybatis.transaction.Transaction;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * ClassName: BaseExecutor
 * Package: cn.xuguowen.mybatis.executor
 * Description:基础执行器：为具体的执行器实现提供基础功能，包括事务管理、查询执行等。体现了模版模式
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:34
 * @Version 1.0
 */
public abstract class BaseExecutor implements Executor {

    // 用于记录日志的 SLF4J 日志对象。
    private org.slf4j.Logger logger = LoggerFactory.getLogger(BaseExecutor.class);

    // 配置对象
    protected Configuration configuration;

    // 管理数据库事务的对象
    protected Transaction transaction;

    // 包装执行器，可以用于装饰模式下的增强功能
    protected Executor wrapper;

    // 表示执行器是否已经关闭的标志
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

    /**
     * 执行 SQL 查询的入口方法
     * @param ms                表示一个映射的 SQL 语句，包含了 SQL 的配置信息。
     * @param parameter         查询参数，用于 SQL 的参数化查询。
     * @param resultHandler     结果处理器，用于处理查询结果。如果不需要特别处理结果集，可以传入 NO_RESULT_HANDLER。
     * @param boundSql          表示绑定的 SQL，包含了生成的 SQL 字符串以及绑定的参数。
     * @return                  返回一个包含查询结果的列表
     * @param <E>
     */
    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        // 检查执行器是否已关闭，如果已关闭则抛出异常。
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }

        return doQuery(ms, parameter, resultHandler, boundSql);
    }

    /**
     * 子类需要实现该方法以执行具体的查询逻辑。通过这种方式，具体的查询细节可以在不同的子类中有不同的实现。
     * @param ms
     * @param parameter
     * @param resultHandler
     * @param boundSql
     * @return
     * @param <E>
     */
    protected abstract <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    /**
     * 获取当前的事务对象。
     * @return
     */
    @Override
    public Transaction getTransaction() {
        // 检查执行器是否已关闭，如果已关闭则抛出异常。
        if (closed) {
            throw new RuntimeException("Executor was closed.");
        }
        return transaction;
    }

    /**
     * 提交事务。
     * @param required      是否需要提交事务。true 表示提交事务，false 表示可能根据某些条件决定不提交。
     * @throws SQLException
     */
    @Override
    public void commit(boolean required) throws SQLException {
        // 检查执行器是否已关闭，如果已关闭则抛出异常。
        if (closed) {
            throw new RuntimeException("Cannot commit, transaction is already closed");
        }
        if (required) {
            transaction.commit();
        }
    }

    /**
     * 回滚事务。
     * @param required      是否需要回滚事务。true 表示回滚事务，false 表示可能根据某些条件决定不回滚。
     * @throws SQLException
     */
    @Override
    public void rollback(boolean required) throws SQLException {
        // 检查执行器是否已关闭，如果已关闭则抛出异常。
        if (!closed) {
            if (required) {
                transaction.rollback();
            }
        }
    }

    /**
     * 关闭执行器。
     * @param forceRollback 是否强制回滚未提交的事务。true 表示强制回滚，false 表示根据事务状态决定是否回滚。
     */
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

}
