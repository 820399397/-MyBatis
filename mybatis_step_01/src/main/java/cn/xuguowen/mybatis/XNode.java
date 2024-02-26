package cn.xuguowen.mybatis;

import java.util.Map;

/**
 * ClassName: XNode
 * Package: cn.xuguowen.mybatis
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/2/8 19:38
 * @Version 1.0
 */
public class XNode {

    private String sql;

    private String resultType;

    private String nameSpace;

    private String parameterType;

    private Map<Integer,String> parameter;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
