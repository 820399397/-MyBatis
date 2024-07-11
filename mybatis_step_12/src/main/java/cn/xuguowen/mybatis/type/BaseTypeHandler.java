package cn.xuguowen.mybatis.type;

import cn.xuguowen.mybatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ClassName: BaseTypeHandler
 * Package: cn.xuguowen.mybatis.type
 * Description:抽象类BaseTypeHandler实现了TypeHandler接口，并提供了基本的配置设置方法。
 * 它定义了一个抽象方法setNonNullParameter，子类需要实现该方法以处理不同类型的参数设置。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:41
 * @Version 1.0
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    // MyBatis的配置对象
    protected Configuration configuration;

    /**
     * 设置MyBatis配置
     *
     * @param configuration MyBatis配置对象
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 设置参数
     * 调用抽象方法setNonNullParameter，由子类实现具体的参数设置逻辑。
     *
     * @param ps PreparedStatement对象，用于执行预编译的SQL语句。
     * @param i 参数的位置，从1开始计数。
     * @param parameter 要设置的参数值，类型为T。
     * @param jdbcType JDBC类型，表示参数在数据库中的类型。
     * @throws SQLException 如果在设置参数时发生SQL异常。
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        // 定义抽象方法，由子类实现不同类型的属性设置
        setNonNullParameter(ps, i, parameter, jdbcType);
    }

    /**
     * 设置非空参数
     * 该抽象方法由子类实现，以处理特定类型的非空参数设置。
     *
     * @param ps PreparedStatement对象，用于执行预编译的SQL语句。
     * @param i 参数的位置，从1开始计数。
     * @param parameter 要设置的参数值，类型为T。
     * @param jdbcType JDBC类型，表示参数在数据库中的类型。
     * @throws SQLException 如果在设置参数时发生SQL异常。
     */
    protected abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        return getNullableResult(rs, columnName);
    }

    protected abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;


}

