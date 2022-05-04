package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
@Profile("prod")
public class InfoControllerProd implements InfoController {

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/getPort")
    public ResponseEntity<Integer> getPort() {
        return ResponseEntity.ok(port);
    }

}
