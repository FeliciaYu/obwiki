package com.example.obwiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.obwiki.rep.*;
import com.example.obwiki.resp.*;
import com.example.obwiki.service.IUserService;
import com.example.obwiki.service.impl.UserServiceImpl;
import com.example.obwiki.utils.SnowFlake;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/obwiki/user")
public class UserController {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private IUserService userService;
    @Autowired
    private SnowFlake snowFlake;
    @Autowired
    private RedisTemplate redisTemplate;

    //获取key值
    @GetMapping("/getSalt")
    public CommonResp<String> getSalt(@RequestParam String loginName) {
        CommonResp<String> resp = new CommonResp<>(true,"查询成功",null);
        UserGetKeyResp salt = userService.getKeyByName(loginName);
        if(salt!=null){
            resp.setSuccess(true);
            resp.setMessage("salt获取成功");
            resp.setContent(salt.getSalt());
        }else {
            resp.setSuccess(false);
            resp.setMessage("用户名不存在");
        }
        return resp;

    }

    //登录
    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req) {
        req.setPassword(req.getPassword());
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token："+ token + "并放入redis中");
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);

        resp.setContent(userLoginResp);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>(true,"查询成功",null);
        PageResp<UserQueryResp> pageResp = userService.listByname(req);
        resp.setContent(pageResp);

        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(req.getPassword());
        CommonResp resp = new CommonResp<>(true,"成功",null);
        userService.save(req);
        return resp;
    }

    @PostMapping("/resetPassword")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(req.getPassword());
        CommonResp resp = new CommonResp<>(true,"成功",null);
        userService.resetPassword(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>(true,"删除成功",null);
        userService.delete(id);
        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token) {
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        LOG.info("从redis中删除token: " + token);
        return resp;
    }

}
