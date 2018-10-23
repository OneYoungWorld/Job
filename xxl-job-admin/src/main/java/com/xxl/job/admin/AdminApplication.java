package com.xxl.job.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.stereotype.Repository;


@SpringBootApplication(scanBasePackages ={"com.xxl.job"} ,exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@MapperScan(annotationClass = Repository.class,basePackages = {"com.xxl.job.admin.dao"})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}