package cn.xuguowen.mybatis.session;


/**
 * ClassName: SqlSessionFactory
 * Package: cn.xuguowen.mybatis.session
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/9 15:50
 * @Version 1.0
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
