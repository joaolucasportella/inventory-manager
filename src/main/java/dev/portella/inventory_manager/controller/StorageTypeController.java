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

import dev.portella.inventory_manager.model.StorageTypeModel;
import dev.portella.inventory_manager.service.StorageTypeService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/storage-type")
public class StorageTypeController {

    private final StorageTypeService storageTypeService;

    private static final String REDIRECT = "redirect:/storage-type";
    private static final String FORM = "/storage_type/form";
    private static final String LIST = "/storage_type/list";
    private static final String SEARCH = "/storage_type/search";
    private static final String STORAGE = "storageType";

    public StorageTypeController(StorageTypeService storageTypeService) {
        this.storageTypeService = storageTypeService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        Page<StorageTypeModel> storageType = this.storageTypeService.findPaginated(page, size);

        model.addAttribute(STORAGE, storageType.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", storageType.getTotalPages());
        model.addAttribute("hasNextPage", storageType.hasNext());
        return LIST;
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        Optional<StorageTypeModel> storageType = this.storageTypeService.findById(id);

        if (storageType.isEmpty()) {
            model.addAttribute("errorMessage", "storageType.notFound");
            return SEARCH;
        }

        model.addAttribute(STORAGE, List.of(storageType.get()));
        return SEARCH;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(STORAGE, new StorageTypeModel());
        return FORM;
    }

    @PostMapping
    public String save(@Valid @ModelAttribute StorageTypeModel storageType, BindingResult result) {
        if (result.hasErrors()) {
            return FORM;
        }

        this.storageTypeService.save(storageType);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute(STORAGE, this.storageTypeService.findByIdOrThrow(id));
        return FORM;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        this.storageTypeService.deleteById(id);
        return REDIRECT;
    }
}
