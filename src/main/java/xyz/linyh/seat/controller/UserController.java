package xyz.linyh.seat.controller;

import jakarta.servlet.http.HttpServletRequest;
import xyz.linyh.seat.common.BaseResponse;
import xyz.linyh.seat.common.Constant;
import xyz.linyh.seat.common.ErrorCode;
import xyz.linyh.seat.common.ResultUtils;
import xyz.linyh.seat.model.user.dto.LoginDto;
import xyz.linyh.seat.model.user.entity.User;
import xyz.linyh.seat.service.UserService;
import xyz.linyh.seat.utils.JwtUtils;
import xyz.linyh.yhspring.annotation.*;

@YhController
@YhRequestMapping("/user")
public class UserController {

    @YhAutoWrite
    private UserService userService;

    @YhPostMapping("/login")
    public BaseResponse Login(@YhRequestBody LoginDto dto) {
        User user = userService.userLogin(dto);
        if(user==null){
            return ResultUtils.error(ErrorCode.LOGIN_ERROR);
        }
//        生成token返回给前端
        String token = JwtUtils.generateToken(String.valueOf(user.getId()));
        return ResultUtils.success(token);
    }

    /**
     * 获取用户登录信息
     * @return
     */
    @YhGetMapping("/info")
    public BaseResponse<User> getInfo(HttpServletRequest request){
        String userId = request.getHeader(Constant.REQUEST_USERID);
        if(userId==null){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR,"用户未登录");
        }
//        查询获取用户登录信息
        User user = userService.getUserById(userId);
        if(user==null){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        }

        return ResultUtils.success(user);

    }

}
