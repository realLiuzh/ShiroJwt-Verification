package com.lzh.util;

import com.lzh.exception.CustomException;
import com.lzh.exception.CustomExceptionType;

/**
 * 断言工具类
 *
 * @author 志昊的刘
 * @date 2021/12/23
 */
public class AssertUtil {

    /**
     * 对抛出异常逻辑的封装
     */
    public static void isTrue(boolean flag, CustomExceptionType e) {
        if (flag)
            throw new CustomException(e);
    }
}
