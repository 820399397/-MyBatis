package cn.xuguowen.mybatis.executor.statement;

import cn.xuguowen.mybatis.executor.Executor;
import cn.xuguowen.mybatis.mapping.BoundSql;
import cn.xuguowen.mybatis.mapping.MappedStatement;
import cn.xuguowen.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * ClassName: PreparedStatementHandler
 * Package: cn.xuguowen.mybatis.executor.statement
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:52
 * @Version 1.0
 */
public class PreparedStatementHandler extends BaseStatementHandler{

    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject,ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = super.boundSql.getSql();
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        // 注意 parameterize 设置参数中还是写死的
        ps.setLong(1, Long.parseLong(((Object[])parameterObject)[0].toString()));
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.<E>handleResultSets(ps);
    }
}
