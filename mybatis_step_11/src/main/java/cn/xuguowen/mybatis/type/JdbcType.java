package cn.xuguowen.mybatis.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: JdbcType
 * Package: cn.xuguowen.mybatis.type
 * Description:枚举类，表示不同的 JDBC 数据类型及其对应的类型代码。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:08
 * @Version 1.0
 */
public enum JdbcType {
    // 定义不同的 JDBC 数据类型及其对应的类型代码
    INTEGER(Types.INTEGER),
    FLOAT(Types.FLOAT),
    DOUBLE(Types.DOUBLE),
    DECIMAL(Types.DECIMAL),
    VARCHAR(Types.VARCHAR),
    CHAR(Types.CHAR),
    TIMESTAMP(Types.TIMESTAMP);

    // JDBC 类型代码
    public final int TYPE_CODE;

    // 用于将类型代码映射到 JdbcType 枚举实例的静态映射
    private static Map<Integer,JdbcType> codeLookup = new HashMap<>();

    // 静态块，将所有的 JdbcType 枚举实例及其对应的类型代码放入 codeLookup 映射中
    static {
        for (JdbcType type : JdbcType.values()) {
            codeLookup.put(type.TYPE_CODE, type);
        }
    }

    /**
     * 构造函数，初始化 JdbcType 枚举实例的类型代码
     *
     * @param code JDBC 类型代码
     */
    JdbcType(int code) {
        this.TYPE_CODE = code;
    }

    /**
     * 根据类型代码获取对应的 JdbcType 枚举实例
     *
     * @param code JDBC 类型代码
     * @return 对应的 JdbcType 枚举实例，如果不存在则返回 null
     */
    public static JdbcType forCode(int code)  {
        return codeLookup.get(code);
    }

}
