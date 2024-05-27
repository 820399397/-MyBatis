package cn.xuguowen.mybatis.executor.statement;

import cn.xuguowen.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * ClassName: StatementHandler
 * Package: cn.xuguowen.mybatis.executor.statement
 * Description:语句处理器是 SQL 执行器中依赖的部分，SQL 执行器封装事务、连接和检测环境等，而语句处理器则是准备语句、参数化传递、执行 SQL、封装结果的处理。
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:43
 * @Version 1.0
 */
public interface StatementHandler {

    /**
     * 准备语句:这通常包括预编译 SQL 语句，以提高执行效率。
     * @param connection        数据库连接对象，用于执行 SQL 语句。
     * @return      准备好的 SQL 语句对象，可能是 Statement、PreparedStatement 或 CallableStatement 的实例。
     * @throws SQLException
     */
    Statement prepare(Connection connection) throws SQLException;

    /**
     * 设置 SQL 语句的参数。对于 PreparedStatement，这通常包括设置参数值。
     * @param statement     SQL 语句对象，通常是 PreparedStatement 或 CallableStatement。
     * @throws SQLException
     */
    void parameterize(Statement statement) throws SQLException;

    /**
     * 执行 SQL 查询并返回结果。查询结果通过 ResultHandler 进行处理和转换。
     * @param statement         执行查询的 SQL 语句对象。
     * @param resultHandler     用于处理查询结果的处理器。
     * @return                  查询结果的列表。每个元素是一个结果对象，类型由 ResultHandler 确定。
     * @param <E>
     * @throws SQLException
     */
    <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;

}
