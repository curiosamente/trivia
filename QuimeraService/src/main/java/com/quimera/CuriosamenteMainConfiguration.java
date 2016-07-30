package com.quimera; /**
 * Created by Manu on 31/1/16.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class CuriosamenteMainConfiguration extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CuriosamenteMainConfiguration.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CuriosamenteMainConfiguration.class, args);
    }

}
