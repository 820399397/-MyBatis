package cn.xuguowen.mybatis.scripting.xmltags;

import java.util.List;

/**
 * ClassName: MixedSqlNode
 * Package: cn.xuguowen.mybatis.scripting.xmltags
 * Description:混合的 SQL 节点，用于包含多个 SQL 节点的组合。
 *
 * @Author 徐国文
 * @Create 2024/5/30 11:05
 * @Version 1.0
 */
public class MixedSqlNode implements SqlNode {

    //组合模式，拥有一个SqlNode的List
    private List<SqlNode> contents;

    // 构造函数，传入包含的 SQL 节点列表
    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    // 应用当前 SQL 节点到指定的上下文
    @Override
    public boolean apply(DynamicContext context) {
        // 依次调用list里每个元素的apply
        contents.forEach(node -> node.apply(context));
        return true;
    }

}

