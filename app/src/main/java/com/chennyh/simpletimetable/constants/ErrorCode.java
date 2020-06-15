package com.chennyh.simpletimetable.constants;

import lombok.Getter;

/**
 * @author Chennyh
 * @date 2020/6/15 10:05
 */
@Getter
public enum ErrorCode {
    USER_NAME_ALREADY_EXIST(1001, "用户名已经存在"),
    EMAIL_ALREADY_EXIST(1002, "邮箱已经存在"),
    NOT_FOUND(1003, "未找到指定资源"),
    USER_ID_NOT_FOUND(1004, "未找到用户ID"),
    COURSE_ID_NOT_FOUND(1005, "未找到课程ID"),
    VERIFY_JWT_FAILED(1006, "token验证失败"),
    METHOD_ARGUMENT_NOT_VALID(1007, "方法参数验证失败");
    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
