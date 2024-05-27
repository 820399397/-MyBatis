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
 * Description:预处理语句处理器
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:52
 * @Version 1.0
 */
public class PreparedStatementHandler extends BaseStatementHandler{

    /**
     * 构造方法，初始化父类的成员变量。
     *
     * @param executor        执行器，用于执行 SQL 语句。
     * @param mappedStatement 映射的 SQL 语句对象，包含了 SQL 语句和相关的配置信息。
     * @param parameterObject 参数对象，包含了执行 SQL 语句时所需的参数。
     * @param resultHandler   结果集处理器，用于处理 SQL 查询的结果集。
     * @param boundSql        绑定的 SQL 对象，包含了 SQL 语句和相关的参数信息。
     */
    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject,ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, boundSql);
    }

    /**
     * 实例化一个 PreparedStatement 对象。
     *
     * @param connection 数据库连接对象。
     * @return 实例化的 PreparedStatement 对象。
     * @throws SQLException 如果 SQL 执行出错。
     */
    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = super.boundSql.getSql();
        return connection.prepareStatement(sql);
    }

    /**
     * 设置 SQL 语句的参数。
     *
     * @param statement 要设置参数的 SQL 语句对象。
     * @throws SQLException 如果 SQL 执行出错。
     */
    @Override
    public void parameterize(Statement statement) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        // 注意 parameterize 设置参数中还是写死的
        // (Object[]) parameterObject:假设 parameterObject 是一个 Object 数组。这一部分代码将 parameterObject 转换为 Object 数组。
        // ((Object[])parameterObject)[0]:从转换后的 Object 数组中取出第一个元素。假设数组中第一个元素是需要设置为 PreparedStatement 参数的值。
        // .toString():将第一个元素转换为字符串。这一步确保无论第一个元素的类型是什么，都可以通过调用 toString 方法将其转换为字符串。
        // Long.parseLong(...):将字符串解析为长整型（long）。这一步确保字符串形式的数字能够转换为 long 类型的数值。
        // ps.setLong(1, Long.parseLong(((Object[])parameterObject)[0].toString()));

        // 改进如上代码：
        Object[] params = (Object[]) parameterObject;
        if (params.length > 0 && params[0] != null) {
            try {
                long value = Long.parseLong(params[0].toString());
                ps.setLong(1, value);
            } catch (NumberFormatException e) {
                throw new SQLException("Invalid parameter format for long value: " + params[0]);
            }
        } else {
            throw new SQLException("No parameter provided for PreparedStatement");
        }
    }

    /**
     * 执行查询，并处理查询结果。
     *
     * @param statement       要执行的 SQL 语句对象。
     * @param resultHandler   结果集处理器，用于处理 SQL 查询的结果集。
     * @param <E>             返回结果的类型。
     * @return 查询结果的列表。
     * @throws SQLException 如果 SQL 执行出错。
     */
    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.<E>handleResultSets(ps);
    }
}
