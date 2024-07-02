package cn.xuguowen.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ClassName: ParameterHandler
 * Package: cn.xuguowen.mybatis.executor.parameter
 * Description:用于处理SQL语句中的参数，提供获取和设置参数的方法。
 *
 * @Author 徐国文
 * @Create 2024/5/30 11:07
 * @Version 1.0
 */
public interface ParameterHandler {

    /**
     * 获取参数对象
     * @return 参数对象
     */
    Object getParameterObject();

    /**
     * 设置SQL语句中的参数
     * @param ps PreparedStatement对象，用于设置参数
     * @throws SQLException 当设置参数过程中发生SQL异常时抛出
     */
    void setParameters(PreparedStatement ps) throws SQLException;

}
