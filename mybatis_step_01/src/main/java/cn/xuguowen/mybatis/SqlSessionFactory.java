package cn.xuguowen.mybatis;

/**
 * ClassName: SqlSessionFactory
 * Package: cn.xuguowen.mybatis
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/8 20:06
 * @Version 1.0
 */
public interface SqlSessionFactory {

    SqlSession openSession();
}
