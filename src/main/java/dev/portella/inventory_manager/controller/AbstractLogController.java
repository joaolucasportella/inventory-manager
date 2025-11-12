package dev.portella.inventory_manager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import dev.portella.inventory_manager.model.LogModel;
import dev.portella.inventory_manager.service.ILogService;

public abstract class AbstractLogController {

    protected final ILogService logService;

    protected final String LIST;
    protected final String SEARCH;
    protected final String ATTR_NAME;

    protected AbstractLogController(ILogService logService, String list,
            String search, String attrName) {
        this.logService = logService;
        this.LIST = list;
        this.SEARCH = search;
        this.ATTR_NAME = attrName;
    }

    protected String doList(int page, int size, Model model) {
        Page<LogModel> pageObj = this.logService.findPaginated(page, size);

        model.addAttribute(ATTR_NAME, pageObj.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("totalPages", pageObj.getTotalPages());
        model.addAttribute("hasNextPage", pageObj.hasNext());
        return LIST;
    }

    protected String doSearchById(String id, Model model, String notFoundKey) {
        Optional<LogModel> opt = this.logService.findById(id);
        if (opt.isEmpty()) {
            model.addAttribute("errorMessage", notFoundKey);
            return SEARCH;
        }

        model.addAttribute(ATTR_NAME, List.of(opt.get()));
        return SEARCH;
    }
}
