package cn.xuguowen.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * ClassName: Transaction
 * Package: cn.xuguowen.mybatis.transaction
 * Description:一次数据库的操作应该具有事务管理能力，而不是通过 JDBC 获取链接后直接执行即可。还应该把控链接、提交、回滚和关闭的操作处理。所以这里我们结合 JDBC 的能力封装事务管理。
 * 定义标准的事务接口，链接、提交、回滚、关闭，具体可以由不同的事务方式进行实现，包括：JDBC和托管事务，托管事务是交给 Spring 这样的容器来管理。
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
