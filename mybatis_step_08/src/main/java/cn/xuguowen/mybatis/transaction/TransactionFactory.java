package cn.xuguowen.mybatis.transaction;


import cn.xuguowen.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * ClassName: TransactionFactory
 * Package: cn.xuguowen.mybatis.transaction
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/15 21:15
 * @Version 1.0
 */
public interface TransactionFactory {
    /**
     * 根据Connection创建一个新事务
     *
     * @param connection
     * @return
     */
    Transaction newTransaction(Connection connection);


    /**
     * 根据数据源、事务隔离级别创建一个新事务
     * @param dataSource
     * @param level
     * @param autoCommit
     * @return
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, Boolean autoCommit);

}
