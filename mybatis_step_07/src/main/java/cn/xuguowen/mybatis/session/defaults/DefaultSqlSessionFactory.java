package cn.xuguowen.mybatis.session.defaults;

import cn.xuguowen.mybatis.executor.Executor;
import cn.xuguowen.mybatis.mapping.Environment;
import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.SqlSession;
import cn.xuguowen.mybatis.session.SqlSessionFactory;
import cn.xuguowen.mybatis.session.TransactionIsolationLevel;
import cn.xuguowen.mybatis.transaction.Transaction;
import cn.xuguowen.mybatis.transaction.TransactionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.sql.SQLException;

/**
 * ClassName: DefaultSqlSessionFactory
 * Package: cn.xuguowen.mybatis.session.defaults
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:50
 * @Version 1.0
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction tx = null;
        try {
            final Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
            // 创建执行器 SimpleExecutor
            final Executor executor = configuration.newExecutor(tx);
            // 创建DefaultSqlSession
            return new DeafultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert tx != null;
                tx.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }
    }
}
