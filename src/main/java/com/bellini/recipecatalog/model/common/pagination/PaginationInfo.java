package com.bellini.recipecatalog.model.common.pagination;

public class PaginationInfo {

    private static final long DEFAULT_OFFSET = 0;
    private static final int DEFAULT_LIMIT = 10;

    private long offset = DEFAULT_OFFSET;
    private int limit = DEFAULT_LIMIT;

    public PaginationInfo(long offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}
