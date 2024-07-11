package cn.xuguowen.mybatis.test;

import cn.xuguowen.mybatis.dao.UserDao;
import cn.xuguowen.mybatis.io.Resources;
import cn.xuguowen.mybatis.pojo.User;
import cn.xuguowen.mybatis.session.SqlSession;
import cn.xuguowen.mybatis.session.SqlSessionFactory;
import cn.xuguowen.mybatis.session.SqlSessionFactoryBuilder;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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

    private UserDao userDao = null;

    private SqlSession sqlSession = null;

    @Before
    public void before() throws IOException {
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        sqlSession = sqlSessionFactory.openSession();
        userDao = sqlSession.getMapper(UserDao.class);
    }

    @Test
    public void test_queryNameById() {
        User user = userDao.queryNameById(1L);
        logger.info("user:{}",JSON.toJSONString(user));
    }

    @Test
    public void test_queryByName() {
        User user = userDao.queryByName("国文哥");
        logger.info("user:{}",JSON.toJSONString(user));
    }

    @Test
    public void test_selectOne() {
        User user = new User();
        user.setUserId("10001");
        user.setUserName("国文哥");
        user.setId(1L);
        User userDB = userDao.selectOne(user);
        logger.info("userDB:{}",JSON.toJSONString(userDB));
    }

    @Test
    public void test_selectAll() {
        List<User> users = userDao.selectAll();
        logger.info("users:{}",JSON.toJSONString(users));
    }

    @Test
    public void test_updateUser() {
        User user = new User();
        user.setUserId("10001");
        user.setId(3L);
        user.setUserName("国文哥");
        int affected = userDao.updateUser(user);
        logger.info("affected:{}",affected);
        // 提交事务
        sqlSession.commit();
    }

    @Test
    public void test_insertUser() {
        User user = new User();
        user.setUserId("10002");
        user.setUserName("飞哥");
        user.setUserHead("1_05");
        userDao.insertUser(user);
        // 提交事务
        sqlSession.commit();
    }

    @Test
    public void test_deleteUser() {
        int affected = userDao.deleteUser("10001");
        logger.info("affected:{}",affected);
        // 提交事务
        sqlSession.commit();
    }

}
