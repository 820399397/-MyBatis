package cn.xuguowen.mybatis.executor.statement;

import cn.xuguowen.mybatis.executor.Executor;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * ClassName: SimpleStatementHandler
 * Package: cn.xuguowen.mybatis.executor.statement
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/5/30 11:47
 * @Version 1.0
 */
public class SimpleStatementHandler extends BaseStatementHandler{

    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, resultHandler,boundSql);
    }


    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {

    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        return null;
    }
}
