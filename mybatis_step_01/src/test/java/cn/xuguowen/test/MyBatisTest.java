package cn.xuguowen.test;

import cn.xuguowen.mybatis.DefaultSqlSessionFactory;
import cn.xuguowen.mybatis.SqlSession;
import cn.xuguowen.mybatis.SqlSessionFactory;
import cn.xuguowen.mybatis.SqlSessionFactoryBuilder;
import cn.xuguowen.pojo.User;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.io.Resources;
import org.junit.Test;

import java.io.Reader;
import java.util.List;

/**
 * ClassName: MyBatisTest
 * Package: cn.xuguowen.test
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/8 21:27
 * @Version 1.0
 */
public class MyBatisTest {
    @Test
    public void test_queryById() {
        String resource = "mybatis-config-datasource.xml";
        Reader reader;
        try {
            reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);

            SqlSession sqlSession = sessionFactory.openSession();
            try {
                List<User> userList = sqlSession.selectList("cn.xuguowen.dao.UserDao.queryAll");
                System.out.println(JSON.toJSONString(userList));
            } finally {
                sqlSession.close();
                reader.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
