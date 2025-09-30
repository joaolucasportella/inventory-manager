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

import dev.portella.inventory_manager.model.StorageAreaModel;
import dev.portella.inventory_manager.service.StorageAreaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/storage-area")
public class StorageAreaController {

    private final StorageAreaService storageAreaService;

    private static final String REDIRECT = "redirect:/storage-area";
    private static final String FORM = "/storage_area/form";
    private static final String LIST = "/storage_area/list";
    private static final String SEARCH = "/storage_area/search";
    private static final String STORAGE = "storageArea";

    public StorageAreaController(StorageAreaService storageAreaService) {
        this.storageAreaService = storageAreaService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        Page<StorageAreaModel> storageArea = this.storageAreaService.findPaginated(page, size);

        model.addAttribute(STORAGE, storageArea.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", storageArea.getTotalPages());
        model.addAttribute("hasNextPage", storageArea.hasNext());
        return LIST;
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        Optional<StorageAreaModel> storageArea = this.storageAreaService.findById(id);

        if (storageArea.isEmpty()) {
            model.addAttribute("errorMessage", "storageArea.notFound");
            return SEARCH;
        }

        model.addAttribute(STORAGE, List.of(storageArea.get()));
        return SEARCH;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(STORAGE, new StorageAreaModel());
        return FORM;
    }

    @PostMapping
    public String save(@Valid @ModelAttribute StorageAreaModel storageArea, BindingResult result) {
        if (result.hasErrors()) {
            return FORM;
        }

        this.storageAreaService.save(storageArea);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute(STORAGE, this.storageAreaService.findByIdOrThrow(id));
        return FORM;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        this.storageAreaService.deleteById(id);
        return REDIRECT;
    }
}
