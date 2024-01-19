package fr.univ.lorraine.ufr.mim.m2.gi.mysurvey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MySurveyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySurveyApplication.class, args);
    }
}
