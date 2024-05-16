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
    String queryById(String id);

    String queryByName(String name);
}
