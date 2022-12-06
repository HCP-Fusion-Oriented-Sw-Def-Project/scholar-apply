package com.example.gbdpuserac.controller.uac;


import com.example.gbdpuserac.service.UacUserService;
import com.example.gbdpuserac.service.UacUserTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 登录相关.
 */
@RestController
@RequestMapping("/uac/user/loginInfo")
@Validated
public class UacUserLoginController {
	private final Logger logger = LoggerFactory.getLogger(UacUserLoginController.class);


	@Resource
	private UacUserTokenService uacUserTokenService;

	@Resource
	private UacUserService uacUserService;

}
