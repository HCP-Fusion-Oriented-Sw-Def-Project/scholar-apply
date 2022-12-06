package com.example.gbdpbootcore.exception;

import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * 全局的的异常拦截器
 *
 * @author Administrator
 */
@Slf4j
@RestControllerAdvice
@Component
public class GlobalExceptionHandler implements ThrowsAdvice {

	@Value("${spring.profiles.active}")
	String profile;
	@Value("${spring.application.name}")
	String applicationName;

	/**
	 * 参数非法异常.
	 *
	 * @param e the e
	 *
	 * @return the result
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Result illegalArgumentException(IllegalArgumentException e) {
		log.error("参数非法异常={}", e.getMessage(), e);
		return ResultGenerator.genFailResult(ResultCode.ILLEGAL_PARAMETER, e.getMessage());
	}

	/**
	 * 业务异常.
	 *
	 * @param e the e
	 *
	 * @return the result
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result businessException(BusinessException e) {
		log.error("业务异常={}", e.getMessage(), e);
		int code = e.getCode();
		if (code >= ResultCode.SUCCESS.code()) {
			return ResultGenerator.genFailResult(ResultCode.valueOf(code), e.getMessage());
		}
		return ResultGenerator.genFailResult(ResultCode.API_PROCESSING_FAILED, e.getMessage());
	}

	/**
	 * 方法参数校验
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("方法参数校验异常={}", e.getMessage(), e);
		return ResultGenerator.genFailResult(ResultCode.ILLEGAL_PARAMETER, e.getBindingResult().getFieldError().getDefaultMessage());
	}



	/**
	 * ValidationException
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result handleValidationException(ValidationException e) {
		log.error(e.getMessage(), e);
		return ResultGenerator.genFailResult(ResultCode.ILLEGAL_PARAMETER, e.getCause().getMessage());
	}

	/**
	 * ConstraintViolationException
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result handleConstraintViolationException(ConstraintViolationException e) {
		log.error(e.getMessage(), e);
		return ResultGenerator.genFailResult(ResultCode.ILLEGAL_PARAMETER, e.getMessage());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result handlerNoFoundException(Exception e) {
		log.error(e.getMessage(), e);
		return ResultGenerator.genFailResult(ResultCode.NOT_FOUND, "路径不存在，请检查路径是否正确");
	}

	/**
	 * 全局异常.
	 *
	 * @param e the e
	 *
	 * @return the result
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result exception(Exception e) {
		log.info("保存全局异常信息 ex={}", e.getMessage(), e);
		return ResultGenerator.error(e.getMessage());
	}

	@ExceptionHandler(DuplicateKeyException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result handleDuplicateKeyException(DuplicateKeyException e) {
		log.error(e.getMessage(), e);
		return ResultGenerator.genFailResult(ResultCode.DUPLICATE_KEY, "数据重复，请检查后提交");
	}

}
