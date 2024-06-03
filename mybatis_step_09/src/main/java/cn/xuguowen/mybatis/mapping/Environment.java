package cn.xuguowen.mybatis.mapping;

import cn.xuguowen.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

/**
 * ClassName: Environment
 * Package: cn.xuguowen.mybatis.mapping
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/15 21:27
 * @Version 1.0
 */
public class Environment {

    // 环境id
    private  String id;
    // 事务工厂
    private TransactionFactory transactionFactory;
    // 数据源
    private DataSource dataSource;

    // 构造函数，初始化环境id，事务工厂，数据源
    private Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
        this.id = id;
        this.transactionFactory = transactionFactory;
        this.dataSource = dataSource;
    }


    public static class Builder {
        // 环境id
        private String id;
        // 事务工厂
        private TransactionFactory transactionFactory;
        // 数据源
        private DataSource dataSource;

        public Builder (String id){
            this.id = id;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory){
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Builder dataSource(DataSource dataSource){
            this.dataSource = dataSource;
            return this;
        }

        public String id() {
            return this.id;
        }

        public Environment build() {
            return new Environment(this.id, this.transactionFactory, this.dataSource);
        }
    }

    public String getId(){
        return id;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
