package cn.xuguowen.dao;

import cn.xuguowen.pojo.User;

import java.util.List;

/**
 * ClassName: UserDao
 * Package: cn.xuguowen.dao
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/8 21:20
 * @Version 1.0
 */
public interface UserDao {

    List<User> queryAll();

}
