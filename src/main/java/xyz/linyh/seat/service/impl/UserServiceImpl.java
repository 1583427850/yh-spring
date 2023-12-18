package xyz.linyh.seat.service.impl;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import xyz.linyh.seat.model.user.dto.LoginDto;
import xyz.linyh.seat.model.user.entity.User;
import xyz.linyh.seat.service.UserService;
import xyz.linyh.yhspring.annotation.YhService;

@YhService
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * 用户登录
     *
     * @param dto
     * @return
     */
    @Override
    public User userLogin(LoginDto dto) {
        System.out.println(dto);
        return getUserMock(dto.getUserAccount());
    }

    /**
     * 根据用户id获取用户
     *
     * @param userId
     * @return
     */
    @Override
    public User getUserById(String userId) {
        System.out.println();
        log.info("用户Id为{}",userId);
        return getUserMock("test");
    }

    private User getUserMock(String userAccount){

        User user = new User();
        user.setId(1L);
        user.setUsername("");
        user.setUserAccount(userAccount);
        user.setUserAvatar("");
        user.setGender(0);
        user.setUserRole("");
        user.setUserPassword("");
        user.setStatus(0);
        user.setViolationTime(new Date());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        return user;
    }

}
