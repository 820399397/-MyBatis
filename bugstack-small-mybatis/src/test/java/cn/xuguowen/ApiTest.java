package cn.xuguowen;

import cn.xuguowen.binding.MapperProxy;
import cn.xuguowen.binding.MapperProxyFactory;
import cn.xuguowen.dao.IUserDao;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ApiTest
 * Package: cn.xuguowen
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/1 12:50
 * @Version 1.0
 */
public class ApiTest {

    /**
     * 测试MapperProxyFactory生成的MapperProxy代理对象
     */
    @Test
    public void testMapperProxyFactory() {
        MapperProxyFactory<IUserDao> mapperProxyFactory = new MapperProxyFactory<>(IUserDao.class);
        Map<String,String> sqlSession = new HashMap<>();
        sqlSession.put("cn.xuguowen.dao.IUserDao.queryByName","模拟执行 Mapper.xml 中的 SQL 语句，操作：查询用户名称");
        IUserDao userDao = mapperProxyFactory.newInstance(sqlSession);
        String result = userDao.queryByName("国文");
        System.out.println(JSON.toJSONString(result));
    }

    /**
     * 测试JDK的代理类
     */
    @Test
    public void testProxyClass() {
        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(IUserDao.class.getClassLoader(), new Class[]{IUserDao.class}, (proxy, method, args) -> "你被代理了！");
        System.out.println(userDao.getClass());
        System.out.println(userDao.queryByName("国文"));
    }
}
