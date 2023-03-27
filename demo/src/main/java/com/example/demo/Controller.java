package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
public class Controller {
    private final Environment environment;
    private static Integer callCount = 0;

    @GetMapping("/")
    public String root() throws Exception {
        String port = environment.getProperty("local.server.port");
        callCount++;
        log.info("load balancing :: " + port + "  callCount :: " + callCount);
        return "ALB port :: " + port + " callCount :: " + callCount;
    }
}
