package com.jacky.springbootdemo;

import com.jacky.springbootdemo.config.MasterDataSourceConfiguration;
import com.jacky.springbootdemo.config.SlaveDataSourceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Boot要求main()方法所在的启动类必须放到根package下，命名不做要求
 *
 * 启动Spring Boot应用程序只需要一行代码加上一个注解@SpringBootApplication，该注解实际上又包含了：
 * @SpringBootConfiguration
 * @Configuration
 * @EnableAutoConfiguration
 * @AutoConfigurationPackage
 * @ComponentScan
 * 这样一个注解就相当于启动了自动配置和自动扫描。
 *
 *
 * 前面我们定义的数据源、声明式事务、JdbcTemplate在哪创建的？怎么就可以直接注入到自己编写的UserService中呢？
 * 这些自动创建的Bean就是Spring Boot的特色：AutoConfiguration。
 *
 * 当我们引入spring-boot-starter-jdbc时，启动时会自动扫描所有的XxxAutoConfiguration：
 * DataSourceAutoConfiguration：自动创建一个DataSource，其中配置项从application.yml的spring.datasource读取；
 * DataSourceTransactionManagerAutoConfiguration：自动创建了一个基于JDBC的事务管理器；
 * JdbcTemplateAutoConfiguration：自动创建了一个JdbcTemplate。
 * 因此，我们自动得到了一个DataSource、一个DataSourceTransactionManager和一个JdbcTemplate。
 *
 * 类似的，当我们引入spring-boot-starter-web时，自动创建了：
 * ServletWebServerFactoryAutoConfiguration：自动创建一个嵌入式Web服务器，默认是Tomcat；
 * DispatcherServletAutoConfiguration：自动创建一个DispatcherServlet；
 * HttpEncodingAutoConfiguration：自动创建一个CharacterEncodingFilter；
 * WebMvcAutoConfiguration：自动创建若干与MVC相关的Bean。
 * ...
 *
 * 引入第三方pebble-spring-boot-starter时，自动创建了：
 * PebbleAutoConfiguration：自动创建了一个PebbleViewResolver。
 *
 *
 * Spring Boot大量使用XxxAutoConfiguration来使得许多组件被自动化配置并创建，而这些创建过程又大量使用了Spring的Conditional功能。
 *
 *
 * Spring Boot自动装配功能是通过自动扫描+条件装配实现的，这一套机制在默认情况下工作得很好，
 * 但是，如果我们要手动控制某个Bean的创建，就需要详细地了解Spring Boot自动创建的原理，
 * 很多时候还要跟踪XxxAutoConfiguration，以便设定条件使得某个Bean不会被自动创建。
 */

//// 启动自动配置，但排除指定的自动配置:
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)

@Import({ MasterDataSourceConfiguration.class, SlaveDataSourceConfiguration.class})
public class SpringbootdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootdemoApplication.class, args);
    }

    // -- Mvc configuration ---------------------------------------------------
    @Bean
    WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 映射路径`/static/`到classpath路径:
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/static/");
            }
        };
    }

}
