package cn.xuguowen.mybatis.binding;

import cn.xuguowen.mybatis.dao.UserDao;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: MapperProxyFactoryTest
 * Package: cn.xuguowen.mybatis.binding
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:11
 * @Version 1.0
 */
public class MapperProxyFactoryTest {

    // 测试映射器工厂
    @Test
    public void test_mapperProxy() {
        MapperProxyFactory<UserDao> factory = new MapperProxyFactory<>(UserDao.class);
        Map<String,String> sqlSession = new HashMap<>();
        sqlSession.put("cn.xuguowen.mybatis.dao.UserDao.queryNameById","select name from user where id = ?");
        UserDao userDao = factory.newInstance(sqlSession);
        System.out.println(userDao.queryNameById());
    }
}
