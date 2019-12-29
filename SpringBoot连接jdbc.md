# springboot连接jdbc数据库并且整合Druid数据源

# 一 连接数据库
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
        
        
## 2.配置数据库连接信息  application.yml
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

# 二 整合Druid数据源

## 1.在springboot项目中导入druid数据源依赖[点击查询最新依赖](https://mvnrepository.com/artifact/com.alibaba/druid-spring-boot-starter)
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.20</version>
        </dependency>
        

## 2.相关配置信息 application.yml
        #jdbc的配置
        spring:
          datasource:
            password: 980508
            username: root
            url: jdbc:mysql://localhost:3306/springboot?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=utf8
            driver-class-name: com.mysql.cj.jdbc.Driver
            initialization-mode: always
            #整合Druid数据源
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
                login-username: lihuazhou
                login-password: 980508
                allow: 192.168.182.1


## 3.测试类查看使用的数据源
        @SpringBootTest
        class SpringbootJdbcApplicationTests {

            @Autowired
            private DataSource dataSource;

            @Test
            void contextLoads() throws SQLException {
                System.out.println(dataSource.getClass());
                System.out.println(dataSource.getConnection());
            }

        }
        
## 4.编写一个Druid配置类   DruidConfig
* 需要把配置信息从application.yml中加入到容器中
        @ConfigurationProperties(prefix = "spring.datasource")
                @Bean
                public DataSource druid(){
                    return  new DruidDataSource();
                }
               
* 配置一个Servelet容器来管理后台（Druid的监控）
        
        @Bean
        public ServletRegistrationBean statViewServlet(){
            ServletRegistrationBean bean=new ServletRegistrationBean(new StatViewServlet(),"/druid/*"); //druid后台的路径
            Map<String,String> initParams =new HashMap<>();

            initParams.put("loginUsername","lihuazhou");
            initParams.put("loginPassword","980508");
            initParams.put("allow","");//可以设置为localhost下才能访问，默认是所有都可以访问
            initParams.put("deny","");//默认是不禁用路径
            bean.setInitParameters(initParams);
            return bean;
        }
        
* 配置一个web监控的Filter（拦截一些操作，有点类似拦截器）
        
          @Bean
          public FilterRegistrationBean webStatFilter(){
              FilterRegistrationBean bean = new FilterRegistrationBean();
              bean.setFilter(new WebStatFilter());

              Map<String,String> initParams = new HashMap<>();
              initParams.put("exclusions","*.js,*.css,/druid/*");     //除了这些操作的数据，其他都会被拦截

              bean.setInitParameters(initParams);

              bean.setUrlPatterns(Arrays.asList("/*"));

            return  bean;
        }
