# SpringBoot自定义starter笔记

## 1.创建一个空项目，在里面添加一个启动器和自动配置模块
* 启动器只用来做依赖导入

* 专门来写一个自动配置模块；

* 启动器依赖自动配置模块，项目中引入相应的starter就会引入启动器的所有传递依赖

![](https://github.com/lhzjoker/images/raw/master/img-store/1574561125363.png)  

## 2.创建启动器
        创建starter，选择maven工程即可，只是用于管理依赖，添加对AutoConfiguration模块的依赖
        启动器模块是一个空 JAR 文件，仅提供辅助性依赖管理，这些依赖可能用于自动 装配或者其他类库
        
        命名规约
        官方命名
        
        spring-boot-starter-模块名
        
        eg：spring-boot-starter-web、spring-boot-starter-jdbc、spring-boot-starter-thymeleaf
        
        自定义命名
        
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
