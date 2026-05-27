package com.gamerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerate.dto.UserLoginDTO;
import com.gamerate.dto.UserRegisterDTO;
import com.gamerate.dto.UserUpdateDTO;
import com.gamerate.entity.User;
import com.gamerate.vo.UserLoginVO;
import com.gamerate.vo.UserProfileVO;

public interface UserService extends IService<User> {

    UserProfileVO register(UserRegisterDTO registerDTO);

    UserLoginVO login(UserLoginDTO loginDTO);

    UserProfileVO getCurrentUserProfile();

    UserProfileVO updateCurrentUserProfile(UserUpdateDTO updateDTO);
}
