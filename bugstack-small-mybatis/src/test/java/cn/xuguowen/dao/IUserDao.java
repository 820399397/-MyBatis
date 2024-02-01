package cn.xuguowen.dao;

/**
 * ClassName: IUserDao
 * Package: cn.xuguowen.dao
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/1 12:51
 * @Version 1.0
 */
public interface IUserDao {
    /**
     * JDBC 5步操作
     * MyBatis => SqlSession(IUserDao.class)
     * 接口没有实现类的，代理。
     * JDK 动态代理
     * Proxy = Proxy.newInstance();
     *
     *
     *
     * @param id
     * @return
     */

    String queryById(String id);

    String queryByName(String name);
}
