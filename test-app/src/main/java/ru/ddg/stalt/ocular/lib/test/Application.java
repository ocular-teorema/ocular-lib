package ru.ddg.stalt.ocular.lib.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.ddg.stalt.ocular.lib.OcularLibModule;

@SpringBootApplication
@Import(OcularLibModule.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
