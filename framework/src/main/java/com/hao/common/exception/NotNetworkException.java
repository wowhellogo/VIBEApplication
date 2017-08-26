package com.hao.common.exception;

import java.io.IOException;

/**
 * @Package com.hao.common.exception
 * @作 用:
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年02月16日  13:36
 */


public class NotNetworkException extends IOException {
    public NotNetworkException() {
        super("网络已断开");
    }

    public NotNetworkException(String message) {
        super(message);
    }
}
