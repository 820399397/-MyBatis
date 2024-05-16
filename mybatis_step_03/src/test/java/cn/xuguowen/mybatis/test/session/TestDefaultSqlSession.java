package cn.xuguowen.mybatis.test.session;

import cn.xuguowen.mybatis.binding.MapperRegistry;
import cn.xuguowen.mybatis.dao.StudentDao;
import cn.xuguowen.mybatis.dao.UserDao;
import cn.xuguowen.mybatis.session.SqlSession;
import cn.xuguowen.mybatis.session.SqlSessionFactory;
import cn.xuguowen.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Test;

/**
 * ClassName: TestDefaultSqlSession
 * Package: cn.xuguowen.mybatis.test.session
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:54
 * @Version 1.0
 */
public class TestDefaultSqlSession {

    @Test
    public void test_openSession() {
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMappers("cn.xuguowen.mybatis.dao");

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        System.out.println(userDao.queryNameById());


        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
        System.out.println(studentDao.queryAddressById());

        // 没走代理处理器类中的invoke方法，也就是没有通过代理对象去调用方法
        Object o = sqlSession.selectOne("cn.xuguowen.mybatis.dao.StudentDao.queryAddressById");
        System.out.println(o);

    }
}
