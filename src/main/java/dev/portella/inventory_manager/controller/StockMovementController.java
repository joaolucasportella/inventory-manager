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

import dev.portella.inventory_manager.model.StockMovementModel;
import dev.portella.inventory_manager.service.StockMovementService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/stock-movement")
public class StockMovementController extends AbstractCrudController<StockMovementModel> {

    private static final String REDIRECT = "redirect:/stock-movement";
    private static final String FORM = "/stock_movement/form";
    private static final String LIST = "/stock_movement/list";
    private static final String SEARCH = "/stock_movement/search";
    private static final String STOCK = "stockMovement";

    public StockMovementController(StockMovementService stockMovementService) {
        super(stockMovementService, REDIRECT, FORM, LIST, SEARCH, STOCK);
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        return doList(page, size, model);
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        return doSearchById(id, model, "stockMovement.notFound");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        return doCreateForm(model, new StockMovementModel());
    }

    @PostMapping
    public String save(@Valid @ModelAttribute StockMovementModel stockMovement, BindingResult result) {
        if (result.hasErrors()) {
            return FORM;
        }

        if (stockMovement.getSourceArea() != null && stockMovement.getSourceArea().getAreaId() == null) {
            stockMovement.setSourceArea(null);
        }

        if (stockMovement.getDestinationArea() != null && stockMovement.getDestinationArea().getAreaId() == null) {
            stockMovement.setDestinationArea(null);
        }

        return doSave(stockMovement, result);
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
