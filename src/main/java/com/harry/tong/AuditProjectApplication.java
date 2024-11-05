package com.harry.tong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.harry.tong.mapper")
public class AuditProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuditProjectApplication.class, args);
    }

}
