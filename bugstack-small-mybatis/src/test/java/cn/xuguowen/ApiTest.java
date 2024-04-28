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
     * 我们都知道：
     * 工厂操作相当于把代理的创建给封装起来了，如果不做这层封装，那么每一个创建代理类的操作，都需要自己使用 Proxy.newProxyInstance 进行处理，那么这样的操作方式就显得比较麻烦了。
     * 如下单元测试就是没有使用到工厂创建代理对象
     */
    @Test
    public void testNoMapperProxyFactory() {
        // 创建 sqlSession 对象，模拟数据库连接信息
        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("cn.xuguowen.dao.IUserDao.queryById", "SELECT * FROM users WHERE id = ?");
        sqlSession.put("cn.xuguowen.dao.IUserDao.queryByName", "SELECT * FROM users WHERE name = ?");

        // 创建 IUserDao 接口的代理对象
        IUserDao userDaoProxy = (IUserDao) Proxy.newProxyInstance(
                IUserDao.class.getClassLoader(), // 类加载器
                new Class[]{IUserDao.class},     // 代理接口数组
                new MapperProxy<>(sqlSession, IUserDao.class) // InvocationHandler 实例
        );

        // 调用代理对象的方法，都会经过代理处理器的invoke方法
        String resultById = userDaoProxy.queryById("1");
        System.out.println("Result by ID: " + resultById);

        String resultByName = userDaoProxy.queryByName("xgw");
        System.out.println("Result by Name: " + resultByName);
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
