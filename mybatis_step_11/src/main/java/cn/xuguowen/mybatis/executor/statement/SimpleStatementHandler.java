package cn.xuguowen.mybatis.executor.statement;

import cn.xuguowen.mybatis.executor.Executor;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.ResultHandler;
import cn.xuguowen.mybatis.session.RowBounds;

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

    public SimpleStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
    }



    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        // N/A
    }

    @Override
    public int update(Statement statement) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return statement.getUpdateCount();
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return resultSetHandler.handleResultSets(statement);
    }

}
