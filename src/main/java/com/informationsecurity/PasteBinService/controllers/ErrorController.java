package com.informationsecurity.PasteBinService.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@RequiredArgsConstructor
@Controller
@ControllerAdvice
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private final String EXCEPTION_NAME = "requestException";

    @RequestMapping(path = "/error")
    public ModelAndView defaultErrorMapping(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("error");
        Exception exception;
        int errorCode;
        if (request.getAttribute(EXCEPTION_NAME) != null) {
            exception = (Exception) request.getAttribute(EXCEPTION_NAME);
            errorCode = (int) request.getAttribute("jakarta.servlet.error.status_code");
        } else {
            exception = new RuntimeException("Ты нашел меня:>");
            errorCode = 569;
        }

        modelAndView.addObject("errorCode", errorCode);
        modelAndView.addObject("errorMessage",
                switch (errorCode) { /// продолжение мема Игорь Считает Ошибки
                    case 400 -> "Неверный запрос";
                    case 401 -> "Неавторизован";
                    case 403 -> "Доступ запрещен";
                    case 404 -> "Не найдено";
                    case 500 -> "Внутренняя ошибка сервера";
                    case 502 -> "Плохой шлюз";
                    case 503 -> "Сервис недоступен";
                    case 504 -> "Время ожидания шлюза истекло";
                    case 569 -> "Внимание, анекдот!";
                    default -> "Произошла ошибка";
                });
        if(exception != null) modelAndView.addObject("errorDescription", exception.getMessage());
        else modelAndView.addObject("errorDescription", "Подробностей нет");

        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultHandler(HttpServletRequest request, Exception exception) throws Exception {
        request.setAttribute(EXCEPTION_NAME, exception);
        throw exception;
    }

}
