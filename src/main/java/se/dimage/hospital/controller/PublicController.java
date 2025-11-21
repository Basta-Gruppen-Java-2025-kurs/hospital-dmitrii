package se.dimage.hospital.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.dimage.hospital.config.ConfigProperties;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {
    private final ConfigProperties configProperties;

    @GetMapping
    public ResponseEntity<String> publicPage() {
        StringBuilder helloString = new StringBuilder("Hello!");
        configProperties.getEnvList().forEach(p -> helloString.append("\n<br/>" + p));
        return ResponseEntity.ok(helloString.toString());
    }

    @GetMapping("/env")
    public  ResponseEntity<List<String>> envPage() {
        return ResponseEntity.ok(configProperties.getEnvKeys().stream().map(k -> k + ": " + configProperties.getProperty(k)).toList());
    }
}
