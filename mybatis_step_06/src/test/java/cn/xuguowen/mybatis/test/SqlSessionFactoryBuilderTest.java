package cn.xuguowen.mybatis.test;

import cn.xuguowen.mybatis.dao.UserDao;
import cn.xuguowen.mybatis.datasource.pooled.PooledDataSource;
import cn.xuguowen.mybatis.io.Resources;
import cn.xuguowen.mybatis.pojo.User;
import cn.xuguowen.mybatis.session.SqlSession;
import cn.xuguowen.mybatis.session.SqlSessionFactory;
import cn.xuguowen.mybatis.session.SqlSessionFactoryBuilder;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: SqlSessionFactoryBuilderTest
 * Package: cn.xuguowen.mybatis.test
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/14 23:28
 * @Version 1.0
 */
public class SqlSessionFactoryBuilderTest {
    private Logger logger = LoggerFactory.getLogger(SqlSessionFactoryBuilderTest.class);

    /**
     * 分别测试无池化和有池化
     * @throws IOException
     */
    @Test
    public void test_SqlSessionFactoryBuilder() throws IOException {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        // 3. 测试验证
        for (int i = 0; i < 50; i++) {
            User user = userDao.queryNameById(1L);
            logger.info("测试结果：{}", JSON.toJSONString(user));
        }
    }

    /**
     * 测试连接池
     */
    @Test
    public void test_Pooled() throws SQLException, InterruptedException {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver("com.mysql.cj.jdbc.Driver");
        pooledDataSource.setUrl("jdbc:mysql://localhost:3306/mybatis_source?serverTimeZone=UTC");
        pooledDataSource.setUsername("root");
        pooledDataSource.setPassword("root");

        while (true) {
            Connection connection = pooledDataSource.getConnection();
            System.out.println(connection);
            TimeUnit.SECONDS.sleep(1);
            // connection.close();
        }
    }
}
