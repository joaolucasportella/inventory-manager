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

import dev.portella.inventory_manager.model.StorageAreaModel;
import dev.portella.inventory_manager.service.StorageAreaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/storage-area")
public class StorageAreaController extends AbstractCrudController<StorageAreaModel> {

    private static final String REDIRECT = "redirect:/storage-area";
    private static final String FORM = "/storage_area/form";
    private static final String LIST = "/storage_area/list";
    private static final String SEARCH = "/storage_area/search";
    private static final String STORAGE = "storageArea";

    public StorageAreaController(StorageAreaService storageAreaService) {
        super(storageAreaService, REDIRECT, FORM, LIST, SEARCH, STORAGE);
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        return doList(page, size, model);
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        return doSearchById(id, model, "storageArea.notFound");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        return doCreateForm(model, new StorageAreaModel());
    }

    @PostMapping
    public String save(@Valid @ModelAttribute StorageAreaModel storageArea, BindingResult result) {
        return doSave(storageArea, result);
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
