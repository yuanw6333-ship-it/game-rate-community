package com.gamerate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerate.common.exception.BusinessException;
import com.gamerate.common.result.ResultCode;
import com.gamerate.dto.UserLoginDTO;
import com.gamerate.dto.UserRegisterDTO;
import com.gamerate.dto.UserUpdateDTO;
import com.gamerate.entity.User;
import com.gamerate.mapper.UserMapper;
import com.gamerate.service.UserService;
import com.gamerate.utils.JwtUtils;
import com.gamerate.utils.UserContext;
import com.gamerate.vo.UserLoginVO;
import com.gamerate.vo.UserProfileVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final int USER_STATUS_NORMAL = 1;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileVO register(UserRegisterDTO registerDTO) {
        String username = registerDTO.getUsername().trim();
        if (existsByUsername(username)) {
            throw new BusinessException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(resolveNickname(registerDTO.getNickname(), username));
        user.setStatus(USER_STATUS_NORMAL);
        user.setIsDeleted(0);
        save(user);

        return toProfileVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVO login(UserLoginDTO loginDTO) {
        String username = loginDTO.getUsername().trim();
        User user = getByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Username or password is incorrect");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Username or password is incorrect");
        }
        if (!Integer.valueOf(USER_STATUS_NORMAL).equals(user.getStatus())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "User account is disabled");
        }

        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);

        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        return UserLoginVO.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    @Override
    public UserProfileVO getCurrentUserProfile() {
        Long userId = UserContext.getUserId();
        User user = getNormalUserById(userId);
        return toProfileVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserProfileVO updateCurrentUserProfile(UserUpdateDTO updateDTO) {
        Long userId = UserContext.getUserId();
        User user = getNormalUserById(userId);

        if (StringUtils.hasText(updateDTO.getEmail()) && existsEmailForOtherUser(updateDTO.getEmail().trim(), userId)) {
            throw new BusinessException("Email already exists");
        }

        if (updateDTO.getNickname() != null) {
            user.setNickname(updateDTO.getNickname().trim());
        }
        if (updateDTO.getAvatar() != null) {
            user.setAvatarUrl(updateDTO.getAvatar().trim());
        }
        if (updateDTO.getEmail() != null) {
            user.setEmail(updateDTO.getEmail().trim());
        }

        updateById(user);
        return toProfileVO(user);
    }

    private boolean existsByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).exists();
    }

    private boolean existsEmailForOtherUser(String email, Long userId) {
        return lambdaQuery()
                .eq(User::getEmail, email)
                .ne(User::getId, userId)
                .exists();
    }

    private User getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .last("LIMIT 1"));
    }

    private User getNormalUserById(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "User not found");
        }
        if (!Integer.valueOf(USER_STATUS_NORMAL).equals(user.getStatus())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "User account is disabled");
        }
        return user;
    }

    private String resolveNickname(String nickname, String username) {
        if (StringUtils.hasText(nickname)) {
            return nickname.trim();
        }
        return username;
    }

    private UserProfileVO toProfileVO(User user) {
        return UserProfileVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .email(user.getEmail())
                .bio(user.getBio())
                .status(user.getStatus())
                .build();
    }
}
