package com.example.obwiki.service;

import com.example.obwiki.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.obwiki.rep.UserLoginReq;
import com.example.obwiki.rep.UserQueryReq;
import com.example.obwiki.rep.UserResetPasswordReq;
import com.example.obwiki.rep.UserSaveReq;
import com.example.obwiki.resp.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
public interface IUserService extends IService<User> {

    public PageResp<UserQueryResp> listByname(UserQueryReq req);

    public void save(UserSaveReq req);

    public void delete(Long id);

    public UserGetKeyResp getKeyByName(String loginName);

    public void resetPassword(UserResetPasswordReq req);

    public UserLoginResp login(UserLoginReq req);
}
