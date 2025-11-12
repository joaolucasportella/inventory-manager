package dev.portella.inventory_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.portella.inventory_manager.service.LogService;

@Controller
@RequestMapping("/log")
public class LogController extends AbstractLogController {

    private static final String LIST = "/log/list";
    private static final String SEARCH = "/log/search";
    private static final String PRODUCT = "log";

    public LogController(LogService logService) {
        super(logService, LIST, SEARCH, PRODUCT);
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        return doList(page, size, model);
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        return doSearchById(id, model, "log.notFound");
    }
}
