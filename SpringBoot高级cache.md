

# springboot高级cache

## 目录
* 1.[springboot缓存](#springboot缓存)
    - 1.[JSR107缓存规范](#JSR107缓存规范)
    - 2.[Spring的缓存抽象](#Spring的缓存抽象)
    - 3.[整合项目](#整合项目)
        - 1.[导入相关配置](#导入相关配置)
        - 2.[配置文件](#配置文件)
        - 3.[创建bean实例](#创建bean实例)
        - 4.[创建mapper接口映射数据库](#创建mapper接口映射数据库)
        - 5.[添加注解](#添加注解)
        - 6.[编写service来具体实现mapper中的方法](#编写service来具体实现mapper中的方法)
        - 7.[编写controller测试](#编写controller测试)
        - 8.[测试结果](#测试结果)

# springboot缓存  


缓存的场景

- 临时性数据存储【校验码】
- 避免频繁因为相同的内容查询数据库【查询的信息】

## JSR107缓存规范 

> 用的比较少

Java Caching定义了5个核心接口

- CachingProvider

  定义了创建、配置、获取、管理和控制多个CacheManager。一个应用可以在运行期间访问多个CachingProvider

- CacheManager

  定义了创建、配置、获取、管理和控制多个唯一命名的Cache,这些Cache存在于CacheManage的上下文中，一个CacheManage只被一个CachingProvider拥有

- Cache

  类似于Map的数据结构并临时储存以key为索引的值，一个Cache仅仅被一个CacheManage所拥有

- Entry

  存储在Cache中的key-value对

- Expiry

  存储在Cache的条目有一个定义的有效期，一旦超过这个时间，就会设置过期的状态，过期无法被访问，更新，删除。缓存的有效期可以通过ExpiryPolicy设置。

  ![35.cache](/images2/35.cache.png)

  

## Spring的缓存抽象

包括一些JSR107的注解

CahceManager

Cache

### 1、基本概念

**重要的概念&缓存注解**

|                | 功能                                                         |
| -------------- | :----------------------------------------------------------- |
| Cache          | 缓存接口，定义缓存操作，实现有：RedisCache、EhCacheCache、ConcurrentMapCache等 |
| CacheManager   | 缓存管理器，管理各种缓存（Cache）组件                        |
| @Cacheable     | 针对方法配置，根据方法的请求参数对其结果进行缓存             |
| @CacheEvict    | 清空缓存                                                     |
| @CachePut      | 保证方法被调用，又希望结果被缓存 update，调用，将信息更新缓存 |
| @EnableCaching | 开启基于注解的缓存                                           |
| KeyGenerator   | 缓存数据时key生成的策略                                      |
| serialize      | 缓存数据时value序列化策略                                    |

## 整合项目

springboot 2.2.3+web+mysql+mybatis+cache

### 导入相关配置

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
        
        		<dependency>
        			<groupId>com.alibaba</groupId>
        			<artifactId>druid-spring-boot-starter</artifactId>
        			<version>1.1.20</version>
        		</dependency>
        </dependencies>
        
### 配置文件

application.yml
```
spring:
        datasource:
          username: root
          password: 980508
          url: jdbc:mysql://localhost:3306/spring-cache?serverTimezone=UTC
          driver-class-name: com.mysql.cj.jdbc.Driver
          initialization-mode: always

#开启驼峰命名法
mybatis:
  configuration:
    map-underscore-to-camel-case: true

```

application.properties
```
# 开启日志，打印sql语句
logging.level.com.atguigu.mapper = debug
# 打印配置报告
debug=true
```

### 创建bean实例

Department
```
package com.atguigu.bean;

public class Department {
	
	private Integer id;
	private String departmentName;
	
	
	public Department() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Department(Integer id, String departmentName) {
		super();
		this.id = id;
		this.departmentName = departmentName;
	}
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
	@Override
	public String toString() {
		return "Department [id=" + id + ", departmentName=" + departmentName + "]";
	}
}

```

Employee
```
package com.atguigu.bean;

public class Employee {
	
	private Integer id;
	private String lastName;
	private String email;
	private Integer gender; //性别 1男  0女
	private Integer dId;
	
	
	public Employee() {
		super();
	}

	
	public Employee(Integer id, String lastName, String email, Integer gender, Integer dId) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.dId = dId;
	}
	
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
	@Override
	public String toString() {
		return "Employee [id=" + id + ", lastName=" + lastName + ", email=" + email + ", gender=" + gender + ", dId="
				+ dId + "]";
	}
}

```

### 创建mapper接口映射数据库
DepartmentMapper
```
package com.atguigu.mapper;

import com.atguigu.bean.Department;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface DepartmentMapper {
    @Select("select * from department where id = #{id}")
    public Department getDepById(Integer id);

    @Update("update department set departmentName=#{departmentName} where id=#{id}")
    public void updateDep(Department department);

    @Delete("delete from department where id=#{id}")
    public void deleteDepById(Integer id);

    @Insert("insert into department (departmentName)values(#{departmentName})")
    public void insertDep(Department department);
}

```

EmployeeMapper
```
package com.atguigu.mapper;

import com.atguigu.bean.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface EmployeeMapper {
    @Select("select * from Employee where id = #{id}")
    public Employee getEmpById(Integer id);

    @Update(" update Employee set lastName=#{lastName},email=#{email},gender=#{gender},d_id=#{dId} where id=#{id}")
    public void updateEmp(Employee employee);

    @Delete("delete form Employee where id=#{id}")
    public void deleteEmpById(Integer id);

    @Insert("insert into Employee(lastName,email,gender,d_id)values(#{lastName},#{email},#{gender},#{dId})")
    public void insertEmp(Employee employee);
}

```

### 添加注解

主程序添加注解MapperScan，并且使用@EnableCaching开启缓存
```
@EnableCaching
@MapperScan("com.atguigu.mapper")
@SpringBootApplication
public class SpringBoot01CacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot01CacheApplication.class, args);
	}

}

```

### 编写service来具体实现mapper中的方法

将方法的运行结果进行缓存，以后要是再有相同的数据，直接从缓存中获取，不用调用方法

 CacheManager中管理多个Cache组件，对缓存的真正CRUD操作在Cache组件中，每个缓存组件都有自己的唯一名字；

 属性：

- CacheName/value:指定存储缓存组件的名字
- key:缓存数据使用的key,可以使用它来指定。默认是使用方法参数的值，1-方法的返回值
- 编写Spel表达式：#id 参数id的值， #a0/#p0 #root.args[0]
- keyGenerator:key的生成器，自己可以指定key的生成器的组件id
- key/keyGendertor二选一使用
- cacheManager指定Cache管理器，或者cacheReslover指定获取解析器
- condition:指定符合条件的情况下，才缓存； ex:condition = "#id>0"
- unless：否定缓存，unless指定的条件为true，方法的返回值就不会被缓存，可以获取到结果进行判断  ;  ex:unless = "#result == null"
- sync:是否使用异步模式，unless不支持

```
@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    @Cacheable(cacheNames = "emp")
    public Employee getEmp(Integer id){
        System.out.println("查询"+id+"号员工");
        Employee employee=employeeMapper.getEmpById(id);
        return employee;
    }
}

```

### 编写controller测试
```
@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/emp/{id}")
    public Employee getEmployee(@PathVariable("id") Integer id){
        Employee emp = employeeService.getEmp(id);
        return emp;
    }
}
```

### 测试结果
第一次访问会查询数据库，继续访问缓存中取值
