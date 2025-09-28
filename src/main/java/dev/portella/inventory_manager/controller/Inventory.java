package dev.portella.inventory_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Inventory {

    @GetMapping("/inventory")
    public String index() {
        return "index";
    }
}
