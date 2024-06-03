package cn.xuguowen.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ClassName: TypeHandler
 * Package: cn.xuguowen.mybatis.type
 * Description:TypeHandler接口定义了设置参数的方法，用于在MyBatis框架中处理SQL语句的参数设置。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:40
 * @Version 1.0
 */
public interface TypeHandler<T> {

    /**
     * 设置参数
     * 该方法用于将Java类型的参数设置到PreparedStatement中，以便执行SQL语句。
     *
     * @param ps PreparedStatement对象，用于执行预编译的SQL语句。
     * @param i 参数的位置，从1开始计数。
     * @param parameter 要设置的参数值，类型为T。
     * @param jdbcType JDBC类型，表示参数在数据库中的类型。
     * @throws SQLException 如果在设置参数时发生SQL异常。
     */
    void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

}

