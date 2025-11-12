package dev.portella.inventory_manager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import dev.portella.inventory_manager.service.ICrudService;

public abstract class AbstractCrudController<T> {

    protected final ICrudService<T> service;

    protected final String REDIRECT;
    protected final String FORM;
    protected final String LIST;
    protected final String SEARCH;
    protected final String ATTR_NAME;

    protected AbstractCrudController(ICrudService<T> service, String redirect, String form, String list,
            String search, String attrName) {
        this.service = service;
        this.REDIRECT = redirect;
        this.FORM = form;
        this.LIST = list;
        this.SEARCH = search;
        this.ATTR_NAME = attrName;
    }

    protected String doList(int page, int size, Model model) {
        Page<T> pageObj = this.service.findPaginated(page, size);

        model.addAttribute(ATTR_NAME, pageObj.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageObj.getTotalPages());
        model.addAttribute("hasNextPage", pageObj.hasNext());
        return LIST;
    }

    protected String doSearchById(String id, Model model, String notFoundKey) {
        Optional<T> opt = this.service.findById(id);
        if (opt.isEmpty()) {
            model.addAttribute("errorMessage", notFoundKey);
            return SEARCH;
        }

        model.addAttribute(ATTR_NAME, List.of(opt.get()));
        return SEARCH;
    }

    protected String doCreateForm(Model model, T newInstance) {
        model.addAttribute(ATTR_NAME, newInstance);
        return FORM;
    }

    protected String doSave(T entity, BindingResult result) {
        if (result.hasErrors()) {
            return FORM;
        }

        this.service.save(entity);
        return REDIRECT;
    }

    protected String doEditForm(String id, Model model) {
        model.addAttribute(ATTR_NAME, this.service.findByIdOrThrow(id));
        return FORM;
    }

    protected String doDelete(String id) {
        this.service.deleteById(id);
        return REDIRECT;
    }
}
