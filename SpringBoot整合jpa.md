# SpringBoot整合jpa笔记

## 目录
  * 1.[导入依赖](#导入依赖)
  * 2.[实体类](#实体类)
  * 3.[JpaRepository](#JpaRepository)
  * 4.[配置文件](#配置文件)
  * 5.[创建Controller类](#创建Controller类)
  
  

## 导入依赖
<details>
    <summary>pom.xml</summary>
    <pre><code>
    <dependencies>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
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
        <!--整合jpa导入的依赖-->
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
          <groupId>mysql</groupId>
          <artifactId>mysql-connector-java</artifactId>
          <scope>runtime</scope>
        </dependency>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
      </dependencies>
    </code></pre>
</details>



## JpaRepository

  /**
   * 继承JpaRepository来完成对数据库的操作
   * 泛型是（实体类，主键）
   */
  public interface UserRepository extends JpaRepository<User,Integer>{
  }
      
     
## 实体类
<details>
    <summary>User</summary>
    <pre><code>
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
    </code></pre>
</details>



## 配置文件
<details>
    <summary>application.yml</summary>
    <pre><code>
    spring:
      datasource:
        username: root
        password: 数据库密码
        url: jdbc:mysql://localhost:3306/jpa?serverTimezone=UTC
        driver-class-name: com.mysql.cj.jdbc.Driver
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: true
    </code></pre>
</details>


    
## 创建Controller类

<details>
    <summary>UserController</summary>
    <pre><code>
    @RestController
    public class UserController {

        @Autowired
        UserRepository userRepository;

        @GetMapping("/user/{id}")
        public User getUserById(@PathVariable("id") Integer id){
            User user= userRepository.getOne(id);       //getOne方法使用的懒加载，获取到的只是代理对象，转换为json时会报错
            return user;
        }

        @GetMapping("/user")
        public User insertUser(User user){
            User save = userRepository.save(user);      //插入一条数据
            return save;
        }
    }
    </code></pre>
</details>


## 实现
* 添加User，访问：

http://localhost:8080/user?lastName=zhangsan&email=AA

http://localhost:8080/user?lastName=lisi&email=BB

* 查询用户访问：

http://localhost:8080/user/1
然后你就会发现抛出500错误，原因是getOne方法使用的懒加载，获取到的只是代理对象，转换为json时会报错


### 解决办法
* 1.关闭懒加载，在实体类上加@Proxy(lazy = false)注解
  
  @Entity
  @Table(name = "tbl_user")
  @Proxy(lazy = false)
  public class User
* 2.转json的时候忽略hibernateLazyInitializer和handler属性

  @Entity
  @Table(name = "tbl_user")
  @JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
  public class User 
