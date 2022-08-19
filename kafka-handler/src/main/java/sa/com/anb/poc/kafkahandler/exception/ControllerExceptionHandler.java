package sa.com.anb.poc.kafkahandler.exception;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ControllerExceptionHandler {
	
  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		 return new ErrorMessage(
			        HttpStatus.NOT_FOUND.value(),
			        new Date(),
			        ex.getMessage(),
			        request.getDescription(false));
    
  }
  
  @ExceptionHandler(MyRestTemplateException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ErrorMessage handleMyRestTemplateException(MyRestTemplateException ex, HttpServletRequest request) {
	  log.error("An error happened while calling {} Downstream API: {}", ex.getApi(), ex.toString());
		 return new ErrorMessage(
			        HttpStatus.BAD_REQUEST.value(),
			        new Date(),
			        ex.getMessage(),
			        ex.getError());
    
  }
  
  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
	    return  new ErrorMessage(
	        HttpStatus.INTERNAL_SERVER_ERROR.value(),
	        new Date(),
	        ex.getMessage(),
	        request.getDescription(false));
	    
	  }
}