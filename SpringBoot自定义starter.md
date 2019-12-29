# SpringBoot自定义starter笔记

## 1.创建一个空项目，在里面添加一个启动器和自动配置模块
* 启动器只用来做依赖导入

* 专门来写一个自动配置模块；

* 启动器依赖自动配置模块，项目中引入相应的starter就会引入启动器的所有传递依赖

![](https://github.com/lhzjoker/images/raw/master/img-store/1574561125363.png)  


## 2.创建启动器
* 创建starter，选择maven工程即可，只是用于管理依赖，添加对AutoConfiguration模块的依赖
 启动器模块是一个空 JAR 文件，仅提供辅助性依赖管理，这些依赖可能用于自动 装配或者其他类库
        
        ### 命名规约,官方命名
        
        spring-boot-starter-模块名
        
        eg：spring-boot-starter-web、spring-boot-starter-jdbc、spring-boot-starter-thymeleaf
        
        ### 自定义命名
        
        模块名-spring-boot-starter
        
        eg：mybatis-spring-boot-start
        
        <!--引入自动配置模块-->
        <dependencies>
            <dependency>
                <groupId>com.atguigu.starter</groupId>
                <artifactId>atguigu-spring-boot-starter-autoconfigurer</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </dependency>
        </dependencies>


## 3.创建一个自动配置模块
* 1.和创建普通的springboot项目一样，不需要引入其他的starter
  
* 2.删除多余的文件和依赖（test和SpringBootApplication都不要）

![](https://github.com/lhzjoker/images/raw/master/img-store/QQ图片20191229173437.png)
  
* 3.导入依赖

        <dependencies>
               <!--引入spring-boot-starter；所有的starter的基本配置-->
               <dependency>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-starter</artifactId>
               </dependency>
               <!--可以生成配置类提示文件-->
               <dependency>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-configuration-processor</artifactId>
                 <optional>true</optional>
               </dependency>
        </dependencies>


## 4.如何编写自动配置
        @Configuration //指定这个类是一个配置类
        @ConditionalOnXXX //在指定条件成立的情况下自动配置类生效
        @AutoConfigureAfter //指定自动配置类的顺序
        @Bean //给容器中添加组件
        @ConfigurationPropertie结合相关xxxProperties类来绑定相关的配置
        @EnableConfigurationProperties //让xxxProperties生效加入到容器中
        public class XxxxAutoConfiguration {
        


## 5.创建配置类和自动配置类
        HelloProperties
        HelloServiceAutoConfiguration


## 6.在resources文件夹下创建META-INF/spring.factories

![](https://github.com/lhzjoker/images/raw/master/img-store/1574568406300.png)

    org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    com.atguigu.starter.HelloServiceAutoConfiguration


## 7.安装到本地仓库
        先安装自动配置模块，因为启动器依赖它，所以要后安装


## 8.创建项目测试，选择添加web场景，因为设置是web场景才生效
    创建Controller
    @RestController
    public class HelloController {
    
        @Autowired
        private HelloService helloService;
    
        @RequestMapping("/hello")
        public String sayHello() {
            String hello = helloService.sayHello("华少");
            return hello;
        }
    }

## 9.在配置文件中配置

    atguigu.hello.prefix=ATGUIGU
    atguigu.hello.suffix=HELLO WORLD
