package cn.xuguowen.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ClassName: LongTypeHandler
 * Package: cn.xuguowen.mybatis.type
 * Description:LongTypeHandler是BaseTypeHandler的具体实现类，用于处理Long类型的参数设置。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:42
 * @Version 1.0
 */
public class LongTypeHandler extends BaseTypeHandler<Long> {

    /**
     * 设置非空的Long类型参数
     * 该方法将Long类型的参数设置到PreparedStatement中对应的位置。
     *
     * @param ps PreparedStatement对象，用于执行预编译的SQL语句。
     * @param i 参数的位置，从1开始计数。
     * @param parameter 要设置的Long类型参数值。
     * @param jdbcType JDBC类型，表示参数在数据库中的类型。
     * @throws SQLException 如果在设置参数时发生SQL异常。
     */
    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter);
    }

}

