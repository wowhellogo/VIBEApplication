package com.hao.common.net.result;

import java.io.Serializable;

/**
 * @Package com.hao.common.net.result
 * @作 用:
 * @创 建 人: 林国定 邮箱：linggoudingg@gmail.com
 * @日 期: 2017/8/27 0027
 */

public class Page implements Serializable {
    private int page;//当前页码
    private int page_count;//总共页数
    private int page_size;//每页总条数
    private int page_total;//总共条数

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getPage_total() {
        return page_total;
    }

    public void setPage_total(int page_total) {
        this.page_total = page_total;
    }

    @Override
    public String toString() {
        return "Page{" +
                "page=" + page +
                ", page_count=" + page_count +
                ", page_size=" + page_size +
                ", page_total=" + page_total +
                '}';
    }
}
