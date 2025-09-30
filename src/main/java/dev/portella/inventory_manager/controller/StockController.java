package dev.portella.inventory_manager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
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
public class StockController {

    private final StockService stockService;

    private static final String REDIRECT = "redirect:/stock";
    private static final String FORM = "/stock/form";
    private static final String LIST = "/stock/list";
    private static final String SEARCH = "/stock/search";
    private static final String STOCK = "stock";

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        Page<StockModel> stock = this.stockService.findPaginated(page, size);

        model.addAttribute(STOCK, stock.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", stock.getTotalPages());
        model.addAttribute("hasNextPage", stock.hasNext());
        return LIST;
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        Optional<StockModel> stock = this.stockService.findById(id);

        if (stock.isEmpty()) {
            model.addAttribute("errorMessage", "stock.notFound");
            return SEARCH;
        }

        model.addAttribute(STOCK, List.of(stock.get()));
        return SEARCH;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(STOCK, new StockModel());
        return FORM;
    }

    @PostMapping
    public String save(@Valid @ModelAttribute StockModel stock, BindingResult result) {
        if (result.hasErrors()) {
            return FORM;
        }

        this.stockService.save(stock);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute(STOCK, this.stockService.findByIdOrThrow(id));
        return FORM;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        this.stockService.deleteById(id);
        return REDIRECT;
    }
}
