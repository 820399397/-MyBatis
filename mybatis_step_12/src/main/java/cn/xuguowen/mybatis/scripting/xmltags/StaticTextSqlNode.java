package cn.xuguowen.mybatis.scripting.xmltags;

/**
 * ClassName: StaticTextSqlNode
 * Package: cn.xuguowen.mybatis.scripting.xmltags
 * Description:静态文本 SQL 节点，表示不含参数的固定文本内容。
 *
 * @Author 徐国文
 * @Create 2024/5/30 11:06
 * @Version 1.0
 */
public class StaticTextSqlNode implements SqlNode{
    // 静态文本内容
    private String text;

    /**
     * 构造一个静态文本 SQL 节点。
     *
     * @param text 静态文本内容
     */
    public StaticTextSqlNode(String text) {
        this.text = text;
    }

    /**
     * 将当前静态文本节点应用到指定的动态上下文中。
     *
     * @param context 动态上下文对象，用于处理 SQL 节点的内容
     * @return 应用结果，始终返回 true，表示应用成功
     */
    @Override
    public boolean apply(DynamicContext context) {
        //将文本加入context
        context.appendSql(text);
        return true;
    }

}
