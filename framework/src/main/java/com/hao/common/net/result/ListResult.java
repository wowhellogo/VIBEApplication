package com.hao.common.net.result;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24 0024.
 */
public class ListResult<T> implements Serializable {
    private List<T> rows;
    private long total;//纪录总数量
    private int pages;//页数

    public ListResult() {
    }

    public ListResult(List<T> rows, long total) {
        this.rows = rows;
        this.total = total;
    }

    public ListResult(List<T> rows, long total, int pages) {
        this.rows = rows;
        this.total = total;
        this.pages = pages;
    }

    public ListResult(List<T> rows) {
        this.rows = rows;
        this.total = rows.size();
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "ListResult{" +
                "total=" + total +
                ", rows=" + rows +
                '}';
    }
}
