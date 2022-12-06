package com.example.gbdpuserac.service.impl;

import com.example.gbdpbootcore.util.PublicUtil;
import com.example.gbdpbootcore.util.RedisKeyUtil;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.UacUserTokenMapper;
import com.example.gbdpuserac.model.UacUser;
import com.example.gbdpuserac.model.UacUserToken;
import com.example.gbdpuserac.model.enums.UacUserTokenStatusEnum;
import com.example.gbdpuserac.security.RequestUtil;
import com.example.gbdpuserac.security.SecurityUser;
import com.example.gbdpuserac.service.UacUserService;
import com.example.gbdpuserac.service.UacUserTokenService;
import com.google.common.collect.Maps;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by CodeGenerator on 2018/09/18.
 */
@Service
@Transactional(readOnly = true)
public class UacUserTokenServiceImpl extends BaseService<UacUserToken> implements UacUserTokenService {

    @Resource
    private UacUserTokenMapper uacUserTokenMapper;
    @Resource
    private UacUserService uacUserService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserToken(OAuth2AccessToken token, final SecurityUser principal, HttpServletRequest request) {
        String accessToken = token.getValue();
        String refreshToken = token.getRefreshToken().getValue();
        String userId = principal.getUserId();
        UacUser uacUser = uacUserService.getById(userId);
        final UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        //获取客户端操作系统
        final String os = userAgent.getOperatingSystem().getName();
        //获取客户端浏览器
        final String browser = userAgent.getBrowser().getName();
        final String remoteAddr = RequestUtil.getRemoteAddr(request);

        // 根据IP获取位置信息
        // final String remoteLocation = opcRpcService.getLocationById(remoteAddr);
        // 存入mysql数据库
        UacUserToken uacUserToken = new UacUserToken();
        int accessTokenValidateSeconds = 36000;
        int refreshTokenValiditySeconds = 36000;
        uacUserToken.setOs(os);
        uacUserToken.setBrowser(browser);
        uacUserToken.setAccessToken(accessToken);
        uacUserToken.setAccessTokenValidity(accessTokenValidateSeconds);
        uacUserToken.setLoginIp("127.0.0.1");//待引入opc
        //uacUserToken.setLoginLocation(remoteLocation);
        uacUserToken.setLoginLocation("127.0.0.1待定");
        //todo
        uacUserToken.setPid("");
        uacUserToken.setLoginTime(new Date());
        uacUserToken.setLoginName(principal.getLoginName());
        uacUserToken.setRefreshToken(refreshToken);
        uacUserToken.setRefreshTokenValidity(refreshTokenValiditySeconds);
        uacUserToken.setStatus(UacUserTokenStatusEnum.ON_LINE.getStatus());
        uacUserToken.setUserId(userId);
        uacUserToken.setTokenType("Basic");
        uacUserToken.setCreateBy(principal.getLoginName());
        uacUserToken.setCreateDate(uacUser.getCreateDate());
        uacUserToken.setUpdateDate(new Date());
        uacUserToken.setUpdateBy(principal.getLoginName());
        uacUserToken.setUserName(principal.getLoginName());
        uacUserToken.setId(UUID.randomUUID().toString());
        uacUserTokenMapper.insert(uacUserToken);
    }


    @Override
    public UacUserToken getByAccessToken(String accessToken) {
        UacUserToken userToken = (UacUserToken) redisTemplate.opsForValue().get(RedisKeyUtil.getAccessTokenKey(accessToken));
        if (userToken == null) {
            UacUserToken userTokennew = new UacUserToken();
            userTokennew.setAccessToken(accessToken);
            userTokennew = uacUserTokenMapper.selectOne(userTokennew);
            return userTokennew;
        }
        return userToken;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUacUserToken(UacUserToken uacUserToken) {
        uacUserTokenMapper.updateByPrimaryKeySelective(uacUserToken);
    }
//    @Override
//    public String refreshToken(String accessToken, String refreshToken, HttpServletRequest request) throws HttpProcessException {
//        String token;
//        Map<String, Object> map = new HashMap<>(2);
//        map.put("grant_type", "refresh_token");
//        map.put("refresh_token", refreshToken);
//
////        //插件式配置请求参数（网址、请求参数、编码、client）
////        Header[] headers = HttpHeader.custom().contentType(HttpHeader.Headers.APP_FORM_URLENCODED).authorization(request.getHeader(HttpHeaders.AUTHORIZATION)).build();
////        HttpConfig config = HttpConfig.custom().headers(headers).url(refreshTokenUrl).map(map);
////        token = HttpClientUtil.post(config);
////        JSONObject jsonObj = JSON.parseObject(token);
////        String accessTokenNew = (String) jsonObj.getRoleWithMenu("access_token");
////        String refreshTokenNew = (String) jsonObj.getRoleWithMenu("refresh_token");
////        String loginName = (String) jsonObj.getRoleWithMenu("loginName");
//        // 更新本次token数据
//        UacUserToken tokenDto = this.getByAccessToken(accessToken);
//        tokenDto.setStatus(UacUserTokenStatusEnum.ON_REFRESH.getStatus());
//        UacUser uacUser = uacUserService.getByLoginName(loginName);
//
//        LoginAuthDto loginAuthDto = new LoginAuthDto(uacUser.getId(), uacUser.getLoginName(), uacUser.getUserName(), uacUser.getGroupId(), uacUser.getGroupName());
//        this.updateUacUserToken(tokenDto, loginAuthDto);
//        // 创建刷新token
//        this.saveUserToken(accessTokenNew, refreshTokenNew, loginAuthDto, request);
//        return token;
//    }

    @Override
    public int batchUpdateTokenOffLine() {
        List<Long> idList = uacUserTokenMapper.listOffLineTokenId();
        if (PublicUtil.isEmpty(idList)) {
            return 1;
        }
        Map<String, Object> map = Maps.newHashMap();
        map.put("status", 20);
        map.put("tokenIdList", idList);
        return uacUserTokenMapper.batchUpdateTokenOffLine(map);
    }

}
