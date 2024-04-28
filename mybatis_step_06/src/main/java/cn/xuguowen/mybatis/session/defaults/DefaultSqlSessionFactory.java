package cn.xuguowen.mybatis.session.defaults;

import cn.xuguowen.mybatis.session.Configuration;
import cn.xuguowen.mybatis.session.SqlSession;
import cn.xuguowen.mybatis.session.SqlSessionFactory;

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
        return new DeafultSqlSession(configuration);
    }
}
