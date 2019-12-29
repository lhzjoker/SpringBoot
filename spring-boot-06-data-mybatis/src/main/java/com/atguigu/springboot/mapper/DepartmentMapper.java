package com.atguigu.springboot.mapper;

import com.atguigu.springboot.bean.Department;
import org.apache.ibatis.annotations.*;

//指定这是一个操作数据库的mapper(必须要配置不然无法进行数据库的操作)
//@Mapper 每个进行操作的表都要添加注解，这样会很不方便，可以在主程序下添加MapperScan
public interface DepartmentMapper {

    @Select("select * from department where id=#{id}")
    public Department getDeptById(Integer id);

    @Delete("delete from department where id=#{id}")
    public int deleteDeptById(Integer id);

    //返回的时候无法显示封装的自增属性，需要加注解Options
    @Options(useGeneratedKeys = true,keyProperty = "id")
    //因为建表的时候设置id是自增的，所以只需要插入departmentName就行
    @Insert("insert into department(department_name) values(#{departmentName})")
    public int insertDept(Department department);

    @Update("update department set department_name=#{departmentName} where id=#{id}")
    public int updateDept(Department department);
}
