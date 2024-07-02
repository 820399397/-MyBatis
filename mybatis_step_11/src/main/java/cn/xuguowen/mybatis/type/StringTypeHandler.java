package cn.xuguowen.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ClassName: StringTypeHandler
 * Package: cn.xuguowen.mybatis.type
 * Description:StringTypeHandler是BaseTypeHandler的具体实现类，用于处理String类型的参数设置。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:41
 * @Version 1.0
 */
public class StringTypeHandler extends BaseTypeHandler<String> {

    /**
     * 设置非空的String类型参数
     * 该方法将String类型的参数设置到PreparedStatement中对应的位置。
     *
     * @param ps PreparedStatement对象，用于执行预编译的SQL语句。
     * @param i 参数的位置，从1开始计数。
     * @param parameter 要设置的String类型参数值。
     * @param jdbcType JDBC类型，表示参数在数据库中的类型。
     * @throws SQLException 如果在设置参数时发生SQL异常。
     */
    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    protected String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }


}
