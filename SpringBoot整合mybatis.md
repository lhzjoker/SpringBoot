# SpringBoot整合mybatis

## 1.引入依赖
    <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-thymeleaf</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>2.1.1</version>
            </dependency>

            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
                <exclusions>
                    <exclusion>
                        <groupId>org.junit.vintage</groupId>
                        <artifactId>junit-vintage-engine</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid-spring-boot-starter</artifactId>
                <version>1.1.20</version>
            </dependency>
    </dependencies>
    
* 依赖关系

![](https://github.com/lhzjoker/images/raw/master/img-store/1574423628318.png)  

## 2.项目构建
* 在resources下创建department.sql和employee.sql，项目启动时创建表
    DROP TABLE IF EXISTS `department`;
    CREATE TABLE `department` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `departmentName` varchar(255) DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
    

    DROP TABLE IF EXISTS `employee`;
    CREATE TABLE `employee` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `lastName` varchar(255) DEFAULT NULL,
      `email` varchar(255) DEFAULT NULL,
      `gender` int(2) DEFAULT NULL,
      `d_id` int(11) DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
    
## 3.创建实体类（与数据表对应）
<details>
<summary>department</summary>
<pre><code>
public class Department {
    private Integer id;
    private String departmentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
</code></pre>

</details>
<details>
    <summary>employee</summary>
    <pre><code>
    public class Employee {
    private Integer id;
    private String lastName;
    private String email;
    private Integer gender;
    private Integer dId;

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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }
}
    </code></pre>
 </details>
 

## 4.配置文件（application.yml）
    spring:
      datasource:
        username: root
        password: 980508
        url: jdbc:mysql://localhost:3306/mybatis?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=utf8
        driver-class-name: com.mysql.cj.jdbc.Driver
        initialization-mode: always
        type: com.alibaba.druid.pool.DruidDataSource
        druid:
          # 连接池配置
          # 配置初始化大小、最小、最大
          initial-size: 1
          min-idle: 1
          max-active: 20
          # 配置获取连接等待超时的时间
          max-wait: 3000
          validation-query: SELECT 1 FROM DUAL
          test-on-borrow: false
          test-on-return: false
          test-while-idle: true
          pool-prepared-statements: true
          time-between-eviction-runs-millis: 60000
          min-evictable-idle-time-millis: 300000
          filters: stat,wall,slf4j
          # 配置web监控,默认配置也和下面相同(除用户名密码，enabled默认false外)，其他可以不配
          web-stat-filter:
            enabled: true
            url-pattern: /*
            exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"
          stat-view-servlet:
            enabled: true
            url-pattern: /druid/*
            login-username: admin
            login-password: root
            allow: 127.0.0.1
    #    schema:
    #建表需要注释掉，不然每次程序一运行就会启动
    #      - classpath:/sql/department.sql
    #      - classpath:/sql/employee.sql
    
    
# mybatis增删改查操作

## 一：使用注解版

## 1.创建mapper接口
