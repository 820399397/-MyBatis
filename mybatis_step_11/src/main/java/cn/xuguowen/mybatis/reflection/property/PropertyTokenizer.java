package cn.xuguowen.mybatis.reflection.property;

import java.util.Iterator;

/**
 * ClassName: PropertyTokenizer
 * Package: cn.xuguowen.mybatis.reflection.property
 * Description:属性分词器，用于解析复合属性表达式（例如"班级[0].学生.成绩"）并将其拆分为独立的属性部分。
 *
 * @Author 徐国文
 * @Create 2024/3/4 15:47
 * @Version 1.0
 */
public class PropertyTokenizer implements Iterable<PropertyTokenizer>, Iterator<PropertyTokenizer> {

    // 例子：班级[0].学生.成绩
    // 属性名，例如：班级
    private String name;

    // 带索引的属性名，例如：班级[0]
    private String indexedName;

    // 索引值，例如：0
    private String index;

    // 子属性部分，例如：学生.成绩
    private String children;

    /**
     * 构造函数，解析复合属性表达式。
     * @param fullname 复合属性表达式，例如"班级[0].学生.成绩"
     */
    public PropertyTokenizer(String fullname) {
        // 班级[0].学生.成绩
        // 找这个点 .
        int delim = fullname.indexOf('.');
        if (delim > -1) {
            name = fullname.substring(0, delim);
            children = fullname.substring(delim + 1);
        } else {
            // 找不到.的话，取全部部分
            name = fullname;
            children = null;
        }
        indexedName = name;
        // 把中括号里的数字给解析出来
        delim = name.indexOf('[');
        if (delim > -1) {
            index = name.substring(delim + 1, name.length() - 1);
            name = name.substring(0, delim);
        }
    }

    /**
     * 获取属性名。
     * @return 属性名
     */
    public String getName() {
        return name;
    }

    /**
     * 获取索引值。
     * @return 索引值
     */
    public String getIndex() {
        return index;
    }

    /**
     * 获取带索引的属性名。
     * @return 带索引的属性名
     */
    public String getIndexedName() {
        return indexedName;
    }

    /**
     * 获取子属性部分。
     * @return 子属性部分
     */
    public String getChildren() {
        return children;
    }

    /**
     * 判断是否还有子属性。
     * @return 如果还有子属性，返回 true；否则返回 false
     */
    @Override
    public boolean hasNext() {
        return children != null;
    }

    /**
     * 获取下一个属性分词器。
     * @return 下一个属性分词器
     */
    @Override
    public PropertyTokenizer next() {
        return new PropertyTokenizer(children);
    }

    /**
     * 删除操作不支持。
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove is not supported, as it has no meaning in the context of properties.");
    }

    /**
     * 获取迭代器。
     * @return 迭代器
     */
    @Override
    public Iterator<PropertyTokenizer> iterator() {
        return this;
    }
}
