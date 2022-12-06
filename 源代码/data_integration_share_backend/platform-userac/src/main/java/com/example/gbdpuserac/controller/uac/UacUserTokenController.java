package com.example.gbdpuserac.controller.uac;

import com.example.gbdpbootcore.httpResult.Result;
import com.example.gbdpbootcore.httpResult.ResultGenerator;
import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpuserac.core.BaseController;
import com.example.gbdpuserac.model.UacUserToken;
import com.example.gbdpuserac.service.UacUserTokenService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
* Created by CodeGenerator on 2018/09/18.
*/
@RestController
@RequestMapping("/uac/user/token")
@Validated
public class UacUserTokenController extends BaseController {

    private static final int EXPIRES_IN = 60 * 20;
    @Resource
    private UacUserTokenService uacUserTokenService;

    @Resource
    private TokenStore tokenStore;

    @PostMapping
    public Result save(@RequestBody @Validated(CreateGroup.class) UacUserToken uacUserToken) {
        uacUserTokenService.save(uacUserToken);
        return ResultGenerator.genSuccessResult();
    }
}
