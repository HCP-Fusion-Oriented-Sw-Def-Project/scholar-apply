package com.example.gbdpbootcore.exception;


import com.example.gbdpbootcore.enums.ErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class Uac biz exception.
 */
public class UacBizException extends BusinessException {
	private final Logger logger = LoggerFactory.getLogger(UacBizException.class);

	private static final long serialVersionUID = -6552248511084911254L;

	/**
	 * Instantiates a new Uac rpc exception.
	 */
	public UacBizException() {
	}

	/**
	 * Instantiates a new Uac rpc exception.
	 *
	 * @param code      the code
	 * @param msgFormat the msg format
	 * @param args      the args
	 */
	public UacBizException(int code, String msgFormat, Object... args) {
		super(code, msgFormat, args);
		logger.info("<== UacRpcException, code:" + this.code + ", message:" + super.getMessage());

	}

	/**
	 * Instantiates a new Uac rpc exception.
	 *
	 * @param code the code
	 * @param msg  the msg
	 */
	public UacBizException(int code, String msg) {
		super(code, msg);
		logger.info("<== UacRpcException, code:" + this.code + ", message:" + super.getMessage());
	}

	/**
	 * Instantiates a new Uac rpc exception.
	 *
	 * @param codeEnum the code enum
	 */
	public UacBizException(ErrorCodeEnum codeEnum) {
		super(codeEnum.code(), codeEnum.msg());
		logger.info("<== UacRpcException, code:" + this.code + ", message:" + super.getMessage());
	}

	/**
	 * Instantiates a new Uac rpc exception.
	 *
	 * @param codeEnum the code enum
	 * @param args     the args
	 */
	public UacBizException(ErrorCodeEnum codeEnum, Object... args) {
		super(codeEnum, args);
		logger.info("<== OpcRpcException, code:" + this.code + ", message:" + super.getMessage());
	}
}
