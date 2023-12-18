package xyz.linyh.seat.service;

import xyz.linyh.seat.model.user.dto.LoginDto;
import xyz.linyh.seat.model.user.entity.User;

public interface UserService {

    /**
     * 用户登录
     * @param dto
     * @return
     */
    User userLogin(LoginDto dto);

    /**
     * 根据用户id获取用户
     * @return
     */
    User getUserById(String userId);
}
