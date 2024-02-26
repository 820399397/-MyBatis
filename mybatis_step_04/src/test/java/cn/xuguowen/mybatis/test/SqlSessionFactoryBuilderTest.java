package cn.xuguowen.mybatis.test;

import cn.xuguowen.mybatis.dao.UserDao;
import cn.xuguowen.mybatis.io.Resources;
import cn.xuguowen.mybatis.session.SqlSession;
import cn.xuguowen.mybatis.session.SqlSessionFactory;
import cn.xuguowen.mybatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

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
    @Test
    public void test_SqlSessionFactoryBuilder() throws IOException {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        String name = userDao.queryNameById(1L);
        System.out.println(name);

        Object o = sqlSession.selectOne("cn.xuguowen.mybatis.dao.UserDao.queryNameById", 1L);
        System.out.println(o);
    }
}
