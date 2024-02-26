package cn.xuguowen.mybatis.transaction.jdbc;

import cn.xuguowen.mybatis.session.TransactionIsolationLevel;
import cn.xuguowen.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * ClassName: JDBCTransaction
 * Package: cn.xuguowen.mybatis.transaction.jdbc
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/15 21:10
 * @Version 1.0
 */
public class JDBCTransaction implements Transaction {

    protected Connection connection;

    protected DataSource dataSource;

    protected TransactionIsolationLevel level = TransactionIsolationLevel.NONE;

    protected boolean autoCommit;

    public JDBCTransaction(Connection connection) {
        this.connection = connection;
    }

    public JDBCTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        this.dataSource = dataSource;
        this.level = level;
        this.autoCommit = autoCommit;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
            connection.setTransactionIsolation(level.getLevel());
            connection.setAutoCommit(autoCommit);
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        // 如果连接不为空并且不是自动提交，则提交事务
        if (Objects.nonNull(connection) && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        // 如果连接不为空并且不是自动提交，则回滚事务
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        // 如果连接不为空并且不是自动提交，则关闭连接
        if (connection != null && !connection.getAutoCommit()) {
            connection.close();
        }
    }
}
