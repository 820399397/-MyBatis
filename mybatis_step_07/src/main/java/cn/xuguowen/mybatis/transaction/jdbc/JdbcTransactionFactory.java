package cn.xuguowen.mybatis.transaction.jdbc;

import cn.xuguowen.mybatis.session.TransactionIsolationLevel;
import cn.xuguowen.mybatis.transaction.Transaction;
import cn.xuguowen.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * ClassName: JdbcTransactionFactory
 * Package: cn.xuguowen.mybatis.transaction.jdbc
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/15 21:18
 * @Version 1.0
 */
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection connection) {
        return new JDBCTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, Boolean autoCommit) {
        return new JDBCTransaction(dataSource,level,autoCommit);
    }
}
