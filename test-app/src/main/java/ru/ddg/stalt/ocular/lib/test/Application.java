package ru.ddg.stalt.ocular.lib.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import ru.ddg.stalt.ocular.lib.OcularLibModule;

@Slf4j
@SpringBootApplication
@Import(OcularLibModule.class)
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        new SpringApplicationBuilder().web(WebApplicationType.NONE).parent(Application.class).build(args).run(args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
