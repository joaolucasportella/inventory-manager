package dev.portella.inventory_manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute(ERROR, "Erro Interno");
        model.addAttribute(MESSAGE, ex.getMessage());
        return ERROR;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model) {
        model.addAttribute(ERROR, "Requisição Inválida");
        model.addAttribute(MESSAGE, ex.getMessage());
        return ERROR;
    }
}
