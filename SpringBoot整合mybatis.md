# SpringBoot整合mybatis

## 目录

* [引入依赖](#引入依赖)
* [项目构建](#项目构建)
* [创建实体类](#创建实体类)
* [配置文件](#配置文件)

* [mybatis增删改查操作](#mybatis增删改查操作)
    - 1.[使用注解版](#使用注解版)
        + 1.[创建mapper接口](#创建mapper接口)
        + 2.[创建Controller类](#创建Controller类)
        + 3.[Mybatis配置](#Mybatis配置)
        + 4.[Mapper扫描](#Mapper扫描)
    - 2.[使用xml配置整合mybatis](使用xml配置整合mybatis)
        + 1.[创建mybatis配置文件](#创建mybatis配置文件)
        + 2.[创建EmployeeMapper接口](#创建EmployeeMapper接口)
        + 3.[创建EmployMapper映射文件](#创建EmployMapper映射文件)
        + 4.[添加配置文件(application.yml)](#添加配置文件(application.yml))
        + 5.[创建EmployContorller类](#创建EmployController类)
        




## 引入依赖
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

## 项目构建

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
    
## 创建实体类

* 与数据表对应
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
 

## 配置文件

        * application.yml
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

## 使用注解版

## 创建mapper接口

        //指定这是一个操作数据库的mapper(必须要配置不然无法进行数据库的操作)
        //@Mapper 每个进行操作的表都要添加注解，这样会很不方便，可以在主程序下添加MapperScan
        @Mapper
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
        
        
## 创建Controller类
        @RestController
        public class DeptController {

            @Autowired
            DepartmentMapper departmentMapper;

            @GetMapping("/dept/{id}")
            public Department getDepartment(@PathVariable("id")Integer id){
                return departmentMapper.getDeptById(id);
            }

            @GetMapping("/dept")
            public Department insertDepartment(Department department){
                departmentMapper.insertDept(department);
                return department;
            }

            @GetMapping("/dept/delete/{id}")
            public String deleteDepartment(@PathVariable("id") Integer id){
                departmentMapper.deleteDeptById(id);
               return "delete id="+id+" success";
            }
        }
        

## 访问：http://localhost:8080/dept?departmentName=AA 添加一条数据

## 访问：http://localhost:8080/dept/1 获取数据


## Mybatis配置
* 开启驼峰命名法

如果我们的实体类属性和数据表列名一致那么就没什么问题
但如果是这样departName和depart_name

## 访问：http://localhost:8080/dept/1 获取数据
        [{"id":1,"departmentName":null}]
        
由于列表和属性名不一致，所以就没有封装进去，我们表中的列名和实体类属性名都是遵循驼峰命名规则的，可以开启mybatis的开启驼峰命名配置


        mybatis:
          configuration:
            map-underscore-to-camel-case: true
            
            
也可以向spring容器中注入配置类MybatisConfig

        //mybatis配置类
        @Configuration
        public class MybatisConfig {
            //设置驼峰命名法
            @Bean
           public ConfigurationCustomizer configurationCustomizer(){
               return new ConfigurationCustomizer() {
                   @Override
                   public void customize(org.apache.ibatis.session.Configuration configuration) {
                       configuration.setMapUnderscoreToCamelCase(true);
                   }
               };
           }
        }
        

## Mapper扫描

使用@Mapper的类可以被扫描到容器中，但是每个类都加就会太麻烦了，我们有一个可以扫描整个包的注解@MapperScan,加到Spring启动类上


        //这个注解可以扫描到包下的所有mapper
        @MapperScan(value = "com.atguigu.springboot.mapper")
        @SpringBootApplication
        public class SpringBoot06DataMybatisApplication {

            public static void main(String[] args) {
                SpringApplication.run(SpringBoot06DataMybatisApplication.class, args);
            }

        }
        
        
## 使用xml配置整合mybatis

## 创建mybatis配置文件 

    * mybatis-config.xml


            <?xml version="1.0" encoding="UTF-8" ?>
            <!DOCTYPE configuration
                    PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
                    "http://mybatis.org/dtd/mybatis-3-config.dtd">
            <configuration>
                    <settings>
                        <!--开启驼峰命名法-->
                        <setting name="mapUnderscoreToCamelCase" value="true" />
                        <setting name="useGeneratedKeys" value="true"/>
                    </settings>
            </configuration>
        
        
        
## 创建EmployeeMapper接口


        public interface EmployeeMapper {
            public Employee getEmpById(Integer id);

            public void insertEmp(Employee employee);
        }
        
        
## 创建EmployeeMapper.xml映射文件  

        <?xml version="1.0" encoding="UTF-8" ?>
        <!DOCTYPE mapper
                PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
                "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
        <mapper namespace="com.atguigu.springboot.mapper.EmployeeMapper">
            <!-- public Employee getEmpById(Integer id);

            public void insertEmp(Employee employee); -->
            <select id="getEmpById" resultType="com.atguigu.springboot.bean.Employee">
                SELECT * FROM Employee WHERE id=#{id}
            </select>

            <insert id="insertEmp">
                INSERT into Employee (lastName,email,gender,d_id) VALUES (#{lastName},#{email},#{gender},#{dId})
            </insert>
        </mapper>

        
## 添加配置文件(application.yml)

* 指定mybatis配置文件和EmployMapper映射文件的位置

        mybatis:
          config-location: classpath:mybatis/mybatis-config.xml
          mapper-locations: classpath:mybatis/mapper/*.xml
          
          
## 创建EmployController类

        @RestController
        public class EmployeeController {

            @Autowired
            EmployeeMapper employeeMapper;

            @GetMapping("/emp/{id}")
            public Employee getEmpById(@PathVariable("id") Integer id){

                return employeeMapper.getEmpById(id);
            }

            @GetMapping("/emp")
            public Employee insertEmp(Employee employee){
                employeeMapper.insertEmp(employee);
                return employee;
            }
        }

## 程序代码目录结构

![](https://github.com/lhzjoker/images/raw/master/img-store/QQ图片20191230183257.png) 
