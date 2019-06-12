package fi.omat.johneagle.filebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@SpringBootApplication
public class FileBoxApplication {
    /**
     * Bean to enable modern java time dialect in thymeleaf and spring.
     *
     * @return Modern Java time dialect.
     */
    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    public static void main(String[] args) {
        SpringApplication.run(FileBoxApplication.class, args);
    }
}
