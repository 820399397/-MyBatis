package cn.xuguowen.mybatis.dao;

import cn.xuguowen.mybatis.pojo.User;

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

    User queryNameById(Long id);

    User queryByName(String userName);

    User selectOne(User user);

    List<User> selectAll();

    int updateUser(User user);

    void insertUser(User user);

    int deleteUser(String userId);
}
