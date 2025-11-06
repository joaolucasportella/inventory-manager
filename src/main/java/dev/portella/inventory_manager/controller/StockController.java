package dev.portella.inventory_manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.portella.inventory_manager.model.StockModel;
import dev.portella.inventory_manager.service.StockService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/stock")
public class StockController extends AbstractCrudController<StockModel> {

    private static final String REDIRECT = "redirect:/stock";
    private static final String FORM = "/stock/form";
    private static final String LIST = "/stock/list";
    private static final String SEARCH = "/stock/search";
    private static final String STOCK = "stock";

    public StockController(StockService stockService) {
        super(stockService, REDIRECT, FORM, LIST, SEARCH, STOCK);
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        return doList(page, size, model);
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        return doSearchById(id, model, "stock.notFound");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        return doCreateForm(model, new StockModel());
    }

    @PostMapping
    public String save(@Valid @ModelAttribute StockModel stock, BindingResult result) {
        return doSave(stock, result);
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        return doEditForm(id, model);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        return doDelete(id);
    }
}
