package com.example.contacts.exception;

import com.example.contacts.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	protected List<ErrorDto> handleApiMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<ErrorDto> errors = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach(e -> {
			String field = ((FieldError) e).getField();
			String message = e.getDefaultMessage();
			errors.add(new ErrorDto(400, field, message));
		});
		return errors;
	}


}
