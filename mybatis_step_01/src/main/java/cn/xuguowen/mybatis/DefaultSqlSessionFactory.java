package cn.xuguowen.mybatis;


/**
 * ClassName: DefaultSqlSessionFactory
 * Package: cn.xuguowen.mybatis
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/8 20:06
 * @Version 1.0
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory{
    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration.getConnection(),configuration.getMapperElement());
    }
}
