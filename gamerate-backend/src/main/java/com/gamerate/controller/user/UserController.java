package com.gamerate.controller.user;

import com.gamerate.common.result.Result;
import com.gamerate.dto.UserLoginDTO;
import com.gamerate.dto.UserRegisterDTO;
import com.gamerate.dto.UserUpdateDTO;
import com.gamerate.service.UserService;
import com.gamerate.vo.UserLoginVO;
import com.gamerate.vo.UserProfileVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<UserProfileVO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        return Result.success(userService.register(registerDTO));
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        return Result.success(userService.login(loginDTO));
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/profile")
    public Result<UserProfileVO> profile() {
        return Result.success(userService.getCurrentUserProfile());
    }

    @Operation(summary = "修改当前用户信息")
    @PutMapping("/profile")
    public Result<UserProfileVO> updateProfile(@Valid @RequestBody UserUpdateDTO updateDTO) {
        return Result.success(userService.updateCurrentUserProfile(updateDTO));
    }
}
