package com.luanpaiva.observador_de_precos_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String hello() {
        return "API funcionando 🚀";
    }

}
