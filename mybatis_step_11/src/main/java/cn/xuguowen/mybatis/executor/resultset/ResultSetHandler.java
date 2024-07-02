package cn.xuguowen.mybatis.executor.resultset;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * ClassName: ResultSetHandler
 * Package: cn.xuguowen.mybatis.executor.resultset
 * Description:结果集处理器
 *
 * @Author 徐国文
 * @Create 2024/2/29 12:47
 * @Version 1.0
 */
public interface ResultSetHandler {
    <E> List<E> handleResultSets(Statement stmt) throws SQLException;
}
