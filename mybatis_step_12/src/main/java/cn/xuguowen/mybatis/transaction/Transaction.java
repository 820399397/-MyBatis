package cn.xuguowen.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ClassName: Transaction
 * Package: cn.xuguowen.mybatis.transaction
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/15 21:09
 * @Version 1.0
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
