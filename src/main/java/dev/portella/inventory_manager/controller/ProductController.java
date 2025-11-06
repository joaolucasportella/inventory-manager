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

import dev.portella.inventory_manager.model.ProductModel;
import dev.portella.inventory_manager.service.ProductService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractCrudController<ProductModel> {

    private static final String REDIRECT = "redirect:/product";
    private static final String FORM = "/product/form";
    private static final String LIST = "/product/list";
    private static final String SEARCH = "/product/search";
    private static final String PRODUCT = "product";

    public ProductController(ProductService productService) {
        super(productService, REDIRECT, FORM, LIST, SEARCH, PRODUCT);
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        return doList(page, size, model);
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        return doSearchById(id, model, "product.notFound");
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        return doCreateForm(model, new ProductModel());
    }

    @PostMapping
    public String save(@Valid @ModelAttribute ProductModel product, BindingResult result) {
        return doSave(product, result);
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
