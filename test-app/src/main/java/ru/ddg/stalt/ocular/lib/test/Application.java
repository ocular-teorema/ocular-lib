package ru.ddg.stalt.ocular.lib.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import ru.ddg.stalt.ocular.lib.OcularLibModule;
import ru.ddg.stalt.ocular.lib.model.Connection;
import ru.ddg.stalt.ocular.lib.services.OcularService;
import ru.ddg.stalt.ocular.lib.model.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@SpringBootApplication
@Import(OcularLibModule.class)
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .web(WebApplicationType.NONE)
                .parent(Application.class)
                .build(args)
                .run(args);
    }

    @Autowired
    private OcularService ocularService;

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = ocularService.connect("78.1.2.3", 1234, "test", "test", 3000, "1")) {

            List<Camera> cameraList = ocularService.getCameraList(connection,"448A5B2C2B54");
            Optional<Camera> runningCamera;
            String streamAddress;
            String cameraId;

            if (cameraList.isEmpty()) {
                Camera camera = new Camera();
                camera.setName("camera_test");
                camera.setPrimaryAddress("test");
                camera.setStatus(CameraStatusEnum.RUNNING);
                camera.setEnabled(true);
                camera.setStorageDays(7);
                camera.setAnalysisType(2);
                camera.setStorageId(1);
                ocularService.addCamera(connection,camera,"448A5B2C2B54");

                runningCamera = ocularService.getCameraList(connection,"448A5B2C2B54")
                        .stream()
                        .findAny();
            }
            else {
                runningCamera = cameraList
                        .stream()
                        .filter(camera -> (camera.getStatus() == CameraStatusEnum.RUNNING))
                        .findAny();
            }
            if (runningCamera.isPresent()) {
                streamAddress = runningCamera.get().getStreamAddress();
                cameraId = runningCamera.get().getCameraId();
                log.info("Camera stream received: {} camera id is {}", streamAddress, cameraId);
            }
        }
    }
}
