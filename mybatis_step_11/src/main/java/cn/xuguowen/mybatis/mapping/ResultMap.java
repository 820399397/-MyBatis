package cn.xuguowen.mybatis.mapping;

import cn.xuguowen.mybatis.session.Configuration;
import org.apache.ibatis.mapping.ResultMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassName: ResultMap
 * Package: cn.xuguowen.mybatis.mapping
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/6/6 15:09
 * @Version 1.0
 */
public class ResultMap {

    private String id;
    private Class<?> type;
    private List<ResultMapping> resultMappings;
    private Set<String> mappedColumns;

    private ResultMap() {
    }

    public static class Builder {
        private ResultMap resultMap = new ResultMap();

        public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings) {
            resultMap.id = id;
            resultMap.type = type;
            resultMap.resultMappings = resultMappings;
        }

        public ResultMap build() {
            resultMap.mappedColumns = new HashSet<>();
            return resultMap;
        }

    }

    public String getId() {
        return id;
    }

    public Set<String> getMappedColumns() {
        return mappedColumns;
    }

    public Class<?> getType() {
        return type;
    }

    public List<ResultMapping> getResultMappings() {
        return resultMappings;
    }


}
