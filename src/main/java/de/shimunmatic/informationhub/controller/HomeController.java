package de.shimunmatic.informationhub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "https://shimunmatic.de", "http://18.192.211.250", "https://corona.shimunmatic.de"})
@RequestMapping("")
public class HomeController {

    @GetMapping(value = "")
    public ResponseEntity<String> getHome(){
        return ResponseEntity.ok("Home");
    }
    @GetMapping(value = "api")
    public ResponseEntity<String> getHomeApi(){
        return ResponseEntity.ok("Home Api");
    }
}
