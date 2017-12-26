package com.hao.common.net.result;

import java.io.Serializable;

/**
 * @Package com.daoda.data_library.repository.net
 * @作 用:包装接口数据
 * @创 建 人: linguoding
 * @日 期: 2016-01-15
 */
public class RESTResult<T> implements Serializable {
    private int errCode;
    private T result;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RESTResult{" +
                "errCode=" + errCode +
                ", result=" + result +
                '}';
    }
}
