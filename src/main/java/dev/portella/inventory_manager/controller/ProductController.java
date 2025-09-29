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

import dev.portella.inventory_manager.model.ProductModel;
import dev.portella.inventory_manager.service.ProductService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    private static final String REDIRECT = "redirect:/product";
    private static final String FORM = "/product/form";
    private static final String LIST = "/product/list";
    private static final String SEARCH = "/product/search";
    private static final String PRODUCT = "product";

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int size,
            Model model) {
        Page<ProductModel> productPage = this.productService.findPaginated(page, size);

        model.addAttribute(PRODUCT, productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("hasNextPage", productPage.hasNext());
        return LIST;
    }

    @GetMapping("/search")
    public String searchById(@RequestParam String id, Model model) {
        Optional<ProductModel> product = this.productService.findById(id);

        if (product.isEmpty()) {
            model.addAttribute("errorMessage", "product.notFound");
            return SEARCH;
        }

        model.addAttribute(PRODUCT, List.of(product.get()));
        return SEARCH;
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(PRODUCT, new ProductModel());
        return FORM;
    }

    @PostMapping
    public String save(@Valid @ModelAttribute ProductModel product, BindingResult result) {
        if (result.hasErrors()) {
            return FORM;
        }

        this.productService.save(product);
        return REDIRECT;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute(PRODUCT, this.productService.findByIdOrThrow(id));
        return FORM;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        this.productService.deleteById(id);
        return REDIRECT;
    }
}
