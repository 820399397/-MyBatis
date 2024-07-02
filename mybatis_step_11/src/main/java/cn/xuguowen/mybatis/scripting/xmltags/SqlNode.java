package cn.xuguowen.mybatis.scripting.xmltags;


/**
 * ClassName: SqlNode
 * Package: cn.xuguowen.mybatis.scripting.xmltags
 * Description:SQL 节点接口，定义了应用 SQL 节点到动态上下文的方法。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:52
 * @Version 1.0
 */
public interface SqlNode {

    /**
     * 应用当前 SQL 节点到指定的动态上下文。
     *
     * @param context 动态上下文对象，用于处理 SQL 节点的内容
     * @return 应用结果，true 表示应用成功，false 表示应用失败
     */
    boolean apply(DynamicContext context);

}

