package com.ecommerce.inventory_service.common.advise;


import com.ecommerce.inventory_service.common.constant.Constant;
import com.ecommerce.inventory_service.common.exception.MainException;
import com.ecommerce.inventory_service.common.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class MainExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = MainException.class)
    public ResponseEntity<BaseResponse> handleMainException(MainException exception) {
        BaseResponse response = new BaseResponse();
        response.setCode(exception.getCode());
        response.setDescription(exception.getMessage());
        response.setAdditionalEntity(null);
        log.warn(String.format("validation code : %s , message : %s ", response.getCode(), response.getDescription()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<BaseResponse> servletRequestBindingException(ServletRequestBindingException exception) {
        BaseResponse response = new BaseResponse(Constant.CODE_VALIDATION, exception.getMessage(), null);
        log.warn(String.format("validation code : %s , message : %s ", response.getCode(), response.getDescription()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn(String.format("%s : %s ", Constant.CODE_VALIDATION,errors));

        return new ResponseEntity<>(new BaseResponse(Constant.CODE_VALIDATION, null, errors), HttpStatus.BAD_REQUEST);
    }
}
