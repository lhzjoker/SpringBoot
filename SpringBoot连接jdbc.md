# springboot连接jdbc数据库

## 1.导入相关依赖

    <!--导入jdbc相关依赖-->
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-thymeleaf</artifactId>
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
        
        
## 2.配置数据库连接信息
    spring:
      datasource:
        username: root
        password: 数据库密码
        url: jdbc:mysql://localhost:3306/springboot?serverTimezone=UTC
        driver-class-name: com.mysql.cj.jdbc.Driver
        
        
## 3.测试有没有连接成功（在测试类中测试）
    @SpringBootTest
    class SpringBootData06JdbcApplicationTests {

      @Autowired
      DataSource dataSource;

      //测试连接数据库
      @Test
      void contextLoads() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
      }
    }
    
* springboot默认是使用com.zaxxer.hikari.HikariDataSource作为数据源，
2.0以下是用org.apache.tomcat.jdbc.pool.DataSource作为数据源；

    如果出现com.zaxxer.hikari.HikariDataSource则表示连接成功
    
    
## 4.数据库查询数据返回网页
    @RestController
    public class JdbcController {

        //查询数据库的数据返回网页
        @Autowired
        JdbcTemplate jdbcTemplate;

        @GetMapping("/query")
        public Map<String,Object> map(){
            List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT * from department");
            return list.get(0);
        }
    }

[查询](http://localhost:8080/query)  
