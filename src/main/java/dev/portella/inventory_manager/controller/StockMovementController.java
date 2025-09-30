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

import dev.portella.inventory_manager.model.StockMovementModel;
import dev.portella.inventory_manager.service.StockMovementService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/stock-movement")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    private static final String REDIRECT = "redirect:/stock-movement";
    private static final String FORM = "/stock_movement/form";
    private static final String LIST = "/stock_movement/list";
    private static final String SEARCH = "/stock_movement/search";
    private static final String STOCK = "stockMovement";

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        Page<StockMovementModel> stockMovement = this.stockMovementService.findPaginated(page, size);

        model.addAttribute(STOCK, stockMovement.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", stockMovement.getTotalPages());
        model.addAttribute("hasNextPage", stockMovement.hasNext());
        return LIST;
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        Optional<StockMovementModel> stockMovement = this.stockMovementService.findById(id);

        if (stockMovement.isEmpty()) {
            model.addAttribute("errorMessage", "stockMovement.notFound");
            return SEARCH;
        }

        model.addAttribute(STOCK, List.of(stockMovement.get()));
        return SEARCH;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(STOCK, new StockMovementModel());
        return FORM;
    }

    @PostMapping
    public String save(@Valid @ModelAttribute StockMovementModel stockMovement, BindingResult result) {
        if (result.hasErrors()) {
            return FORM;
        }

        this.stockMovementService.save(stockMovement);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute(STOCK, this.stockMovementService.findByIdOrThrow(id));
        return FORM;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        this.stockMovementService.deleteById(id);
        return REDIRECT;
    }
}
