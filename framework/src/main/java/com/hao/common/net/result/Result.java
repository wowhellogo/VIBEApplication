package com.hao.common.net.result;

import java.io.Serializable;
import java.util.List;

/**
 * @Package com.daoda.data_library.repository.net
 * @作 用:接口包装的额外信息
 * @创 建 人: linguoding
 * @日 期: 2016-01-15
 */
public class Result<T> implements Serializable {
    private List<T> list;
    //当前页码
    private int currently;
    //总共页数
    private int total;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCurrently() {
        return currently;
    }

    public void setCurrently(int currently) {
        this.currently = currently;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Result{" +
                "list=" + list +
                ", currently=" + currently +
                ", total=" + total +
                '}';
    }
}
