package com.atguigu.srpingboot.bean;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity     //使用JPA注解配置映射关系，告诉JPA这是一个实体类（和数据表映射的类）
@Table(name = "tal_user")       //给表命名，不加这个注解的话，默认的表名就是类名的首字母小写
@Proxy(lazy = false)            //关闭懒加载,getOne方法使用的懒加载，获取到的只是代理对象，转换为json时会报错
public class User {
    @Id     //表示这是一个主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //自增主键
    private Integer id;
    @Column(name = "last_name",length = 50)     //这是和数据表对应的一个类，给他定义列名和长度
    private String lastName;
    @Column     //省略就默认是属性名
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
