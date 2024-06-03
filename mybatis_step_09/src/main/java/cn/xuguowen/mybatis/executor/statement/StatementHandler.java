package cn.xuguowen.mybatis.executor.statement;

import cn.xuguowen.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * ClassName: StatementHandler
 * Package: cn.xuguowen.mybatis.executor.statement
 * Description:语句处理器
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:43
 * @Version 1.0
 */
public interface StatementHandler {

    /**
     * 准备语句
     */
    Statement prepare(Connection connection) throws SQLException;

    /**
     * 参数化
     */
    void parameterize(Statement statement) throws SQLException;

    /**
     * 执行查询
     */
    <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;

}
