package cn.xuguowen.mybatis.session;

import cn.xuguowen.mybatis.builder.xml.XMLConfigBuilder;
import cn.xuguowen.mybatis.executor.BaseExecutor;
import cn.xuguowen.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.LoggerFactory;

import java.io.Reader;

/**
 * ClassName: SqlSessionFactoryBuilder
 * Package: cn.xuguowen.mybatis.session
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/14 20:53
 * @Version 1.0
 */
public class SqlSessionFactoryBuilder {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(SqlSessionFactoryBuilder.class);

    public SqlSessionFactoryBuilder() {
        logger.info("SqlSessionFactoryBuilder is created");
    }

    public SqlSessionFactory build(Reader reader) {
        logger.info("SqlSessionFactory build method is called");
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }

}
