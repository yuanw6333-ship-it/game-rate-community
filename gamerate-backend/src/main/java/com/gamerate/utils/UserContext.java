package com.gamerate.utils;

import com.gamerate.common.exception.BusinessException;
import com.gamerate.common.result.ResultCode;

public final class UserContext {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<String> USERNAME_HOLDER = new ThreadLocal<>();

    private UserContext() {
    }

    public static void set(Long userId, String username) {
        USER_ID_HOLDER.set(userId);
        USERNAME_HOLDER.set(username);
    }

    public static Long getUserId() {
        Long userId = USER_ID_HOLDER.get();
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "User is not logged in");
        }
        return userId;
    }

    public static String getUsername() {
        return USERNAME_HOLDER.get();
    }

    public static void clear() {
        USER_ID_HOLDER.remove();
        USERNAME_HOLDER.remove();
    }
}
