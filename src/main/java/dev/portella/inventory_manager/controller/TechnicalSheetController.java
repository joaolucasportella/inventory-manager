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

import dev.portella.inventory_manager.model.TechnicalSheetModel;
import dev.portella.inventory_manager.service.TechnicalSheetService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/technical-sheet")
public class TechnicalSheetController extends AbstractCrudController<TechnicalSheetModel> {

    private static final String REDIRECT = "redirect:/technical-sheet";
    private static final String FORM = "/technical_sheet/form";
    private static final String LIST = "/technical_sheet/list";
    private static final String SEARCH = "/technical_sheet/search";
    private static final String SHEET = "technicalSheet";

    public TechnicalSheetController(TechnicalSheetService technicalSheetService) {
        super(technicalSheetService, REDIRECT, FORM, LIST, SEARCH, SHEET);
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        return doList(page, size, model);
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        return doSearchById(id, model, "technicalSheet.notFound");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        return doCreateForm(model, new TechnicalSheetModel());
    }

    @PostMapping
    public String save(@Valid @ModelAttribute TechnicalSheetModel technicalSheet, BindingResult result) {
        return doSave(technicalSheet, result);
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
