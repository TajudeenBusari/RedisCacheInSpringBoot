package com.tjtechy.CacheWithRedisInSpringBootApi.product.exception;

import com.tjtechy.CacheWithRedisInSpringBootApi.product.system.Result;
import com.tjtechy.CacheWithRedisInSpringBootApi.product.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * This class is used to handle exceptions in the controller layer
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

  @ExceptionHandler(ProductNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  Result handleProductNotFoundException(ProductNotFoundException e) {
    return new Result(e.getMessage(), false, StatusCode.NOT_FOUND);
  }

  //for invalid data input
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Result handleValidException(MethodArgumentNotValidException e) {
    List<ObjectError> fieldErrors = e.getAllErrors();
    Map<String, String> map = new HashMap<>(fieldErrors.size());
    fieldErrors.forEach(error -> {
      String key = ((FieldError)error).getField();
      String value = ((FieldError) error).getDefaultMessage();
      map.put(key, value);
    });

    return new Result("Provided arguments are invalid, see data for details", false, map, StatusCode.BAD_REQUEST);
  }

  //for any other unhanded exception
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  Result handleOtherException(Exception e) {
    return new Result("A server internal error occurs", false, e.getMessage(), StatusCode.INTERNAL_SERVER_ERROR );
  }
}
