package cn.xuguowen.mybatis.builder;

import java.util.HashMap;

/**
 * ClassName: ParameterExpression
 * Package: cn.xuguowen.mybatis.builder
 * Description:解析MyBatis中的参数表达式，将表达式中的各个属性和值存储到当前对象的键值对中。
 *
 * @Author 徐国文
 * @Create 2024/5/30 10:50
 * @Version 1.0
 */
public class ParameterExpression extends HashMap<String, String> {

    private static final long serialVersionUID = -2417552199605158680L;

    /**
     * 构造方法，根据传入的表达式进行解析
     * @param expression 表达式字符串，例如 "#{property,javaType=int,jdbcType=NUMERIC}"
     */
    public ParameterExpression(String expression) {
        parse(expression);
    }

    /**
     * 解析表达式，将解析后的属性和值存储到当前对象中
     * @param expression 表达式字符串
     */
    private void parse(String expression) {
        // #{property,javaType=int,jdbcType=NUMERIC}
        // 首先去除空白,返回的p是第一个不是空白的字符位置
        int p = skipWS(expression, 0);
        if (expression.charAt(p) == '(') {
            //处理表达式
            expression(expression, p + 1);
        } else {
            //处理属性
            property(expression, p);
        }
    }

    /**
     * 解析表达式
     * 表达式可能是3.2的新功能，可以先不管
     * @param expression 表达式字符串
     * @param left 起始位置
     */
    private void expression(String expression, int left) {
        int match = 1;
        int right = left + 1;
        while (match > 0) {
            if (expression.charAt(right) == ')') {
                match--;
            } else if (expression.charAt(right) == '(') {
                match++;
            }
            right++;
        }
        put("expression", expression.substring(left, right - 1));
        jdbcTypeOpt(expression, right);
    }

    /**
     * 解析属性
     * @param expression 表达式字符串
     * @param left 起始位置
     */
    private void property(String expression, int left) {
        // #{property,javaType=int,jdbcType=NUMERIC}
        // property:VARCHAR
        if (left < expression.length()) {
            //首先，得到逗号或者冒号之前的字符串，加入到property
            int right = skipUntil(expression, left, ",:");
            put("property", trimmedStr(expression, left, right));
            // 第二，处理javaType，jdbcType
            jdbcTypeOpt(expression, right);
        }
    }

    /**
     * 跳过空白字符
     * @param expression 表达式字符串
     * @param p 起始位置
     * @return 第一个非空字符的位置
     */
    private int skipWS(String expression, int p) {
        for (int i = p; i < expression.length(); i++) {
            if (expression.charAt(i) > 0x20) {
                return i;
            }
        }
        return expression.length();
    }

    /**
     * 跳过直到遇到指定字符
     * @param expression 表达式字符串
     * @param p 起始位置
     * @param endChars 结束字符
     * @return 第一个结束字符的位置
     */
    private int skipUntil(String expression, int p, final String endChars) {
        for (int i = p; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (endChars.indexOf(c) > -1) {
                return i;
            }
        }
        return expression.length();
    }

    /**
     * 解析jdbcType或选项
     * @param expression 表达式字符串
     * @param p 起始位置
     */
    private void jdbcTypeOpt(String expression, int p) {
        // #{property,javaType=int,jdbcType=NUMERIC}
        // property:VARCHAR
        // 首先去除空白,返回的p是第一个不是空白的字符位置
        p = skipWS(expression, p);
        if (p < expression.length()) {
            //第一个property解析完有两种情况，逗号和冒号
            if (expression.charAt(p) == ':') {
                jdbcType(expression, p + 1);
            } else if (expression.charAt(p) == ',') {
                option(expression, p + 1);
            } else {
                throw new RuntimeException("Parsing error in {" + expression + "} in position " + p);
            }
        }
    }

    /**
     * 解析jdbcType
     * @param expression 表达式字符串
     * @param p 起始位置
     */
    private void jdbcType(String expression, int p) {
        // property:VARCHAR
        int left = skipWS(expression, p);
        int right = skipUntil(expression, left, ",");
        if (right > left) {
            put("jdbcType", trimmedStr(expression, left, right));
        } else {
            throw new RuntimeException("Parsing error in {" + expression + "} in position " + p);
        }
        option(expression, right + 1);
    }

    /**
     * 解析选项
     * @param expression 表达式字符串
     * @param p 起始位置
     */
    private void option(String expression, int p) {
        // #{property,javaType=int,jdbcType=NUMERIC}
        int left = skipWS(expression, p);
        if (left < expression.length()) {
            int right = skipUntil(expression, left, "=");
            String name = trimmedStr(expression, left, right);
            left = right + 1;
            right = skipUntil(expression, left, ",");
            String value = trimmedStr(expression, left, right);
            put(name, value);
            // 递归调用option，进行逗号后面一个属性的解析
            option(expression, right + 1);
        }
    }

    /**
     * 去除字符串两端的空白字符
     * @param str 字符串
     * @param start 起始位置
     * @param end 结束位置
     * @return 去除空白字符后的字符串
     */
    private String trimmedStr(String str, int start, int end) {
        while (str.charAt(start) <= 0x20) {
            start++;
        }
        while (str.charAt(end - 1) <= 0x20) {
            end--;
        }
        return start >= end ? "" : str.substring(start, end);
    }

}

