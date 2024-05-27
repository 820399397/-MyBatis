package cn.xuguowen.mybatis.executor;

import cn.xuguowen.mybatis.executor.statement.StatementHandler;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.ResultHandler;
import cn.xuguowen.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * ClassName: SimpleExecutor
 * Package: cn.xuguowen.mybatis.executor
 * Description:简单执行器
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:39
 * @Version 1.0
 */
public class SimpleExecutor extends BaseExecutor{


    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
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
    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, resultHandler, boundSql);
            Connection connection = transaction.getConnection();
            Statement stmt = handler.prepare(connection);
            handler.parameterize(stmt);
            return handler.query(stmt, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
