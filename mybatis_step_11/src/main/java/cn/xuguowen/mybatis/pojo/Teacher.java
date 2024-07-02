package cn.xuguowen.mybatis.pojo;

import java.util.List;

/**
 * ClassName: Teacher
 * Package: cn.xuguowen.mybatis.pojo
 * Description:
 *
 * @Author 徐国文
 * @Create 2024/3/4 15:57
 * @Version 1.0
 */
public class Teacher {

    private String name;

    private List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Student {

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
