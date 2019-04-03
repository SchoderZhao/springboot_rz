package com.qf.utils;

import java.util.List;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/26  9:19
 */
public class ResultData {
    private long total;
    private List<?> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public ResultData(long total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public ResultData() {
    }
}
