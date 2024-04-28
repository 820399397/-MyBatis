package cn.xuguowen.mybatis.mapping;

/**
 * ClassName: SqlCommandType
 * Package: cn.xuguowen.mybatis.mapping
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/14 23:13
 * @Version 1.0
 */
public enum SqlCommandType {
    /**
     * 未知
     */
    UNKNOWN,
    /**
     * 插入
     */
    INSERT,
    /**
     * 更新
     */
    UPDATE,
    /**
     * 删除
     */
    DELETE,
    /**
     * 查找
     */
    SELECT;

}
