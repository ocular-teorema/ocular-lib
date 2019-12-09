package ru.ddg.stalt.ocular.lib.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import ru.ddg.stalt.ocular.lib.OcularLibModule;

@Slf4j
@SpringBootApplication
@Import(OcularLibModule.class)
public class Application {
    public static void main(String[] args) {
        log.debug("Start application.");
        SpringApplication.run(Application.class, args);
        log.debug("Application started.");
    }
}
