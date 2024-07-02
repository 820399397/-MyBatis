package cn.xuguowen.mybatis.test;

import cn.xuguowen.mybatis.dao.UserDao;
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

    @Test
    public void test_SqlSessionFactoryBuilder() throws IOException {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        // 3. 测试验证
        // User user = userDao.queryNameById(1L);
        // User user = userDao.queryByName("国文哥");

        User user = userDao.selectOne(new User(1L, "国文哥"));
        logger.info("测试结果：{}", JSON.toJSONString(user));
    }

}
