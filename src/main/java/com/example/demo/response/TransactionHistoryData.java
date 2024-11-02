package com.example.demo.response;

import java.util.List;

public class TransactionHistoryData {
    private int offset;
    private int limit;
    private List<TransactionHistoryResponse> records;

    public TransactionHistoryData(int offset, int limit, List<TransactionHistoryResponse> records) {
        this.offset = offset;
        this.limit = limit;
        this.records = records;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<TransactionHistoryResponse> getRecords() {
        return records;
    }

    public void setRecords(List<TransactionHistoryResponse> records) {
        this.records = records;
    }
}
