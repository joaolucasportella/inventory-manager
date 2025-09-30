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

import dev.portella.inventory_manager.model.TechnicalSheetModel;
import dev.portella.inventory_manager.service.TechnicalSheetService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/technical-sheet")
public class TechnicalSheetController {

    private final TechnicalSheetService technicalSheetService;

    private static final String REDIRECT = "redirect:/technical-sheet";
    private static final String FORM = "/technical_sheet/form";
    private static final String LIST = "/technical_sheet/list";
    private static final String SEARCH = "/technical_sheet/search";
    private static final String SHEET = "technicalSheet";

    public TechnicalSheetController(TechnicalSheetService technicalSheetService) {
        this.technicalSheetService = technicalSheetService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        Page<TechnicalSheetModel> technicalSheetPage = this.technicalSheetService.findPaginated(page, size);

        model.addAttribute(SHEET, technicalSheetPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", technicalSheetPage.getTotalPages());
        model.addAttribute("hasNextPage", technicalSheetPage.hasNext());
        return LIST;
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        Optional<TechnicalSheetModel> technicalSheet = this.technicalSheetService.findById(id);

        if (technicalSheet.isEmpty()) {
            model.addAttribute("errorMessage", "technicalSheet.notFound");
            return SEARCH;
        }

        model.addAttribute(SHEET, List.of(technicalSheet.get()));
        return SEARCH;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(SHEET, new TechnicalSheetModel());
        return FORM;
    }

    @PostMapping
    public String save(@Valid @ModelAttribute TechnicalSheetModel technicalSheet, BindingResult result) {
        if (result.hasErrors()) {
            return FORM;
        }

        this.technicalSheetService.save(technicalSheet);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute(SHEET, this.technicalSheetService.findByIdOrThrow(id));
        return FORM;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        this.technicalSheetService.deleteById(id);
        return REDIRECT;
    }
}
