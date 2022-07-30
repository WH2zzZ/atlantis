package com.oowanghan.atlantis.framework.web.vo;

public class BizPageResponse<T> extends BizResponse<T> {

    public static final int DEFAULT_PAGE_SIZE = 50;

    private Long total; // 总数

    private Integer pageSize; // 页大小

    private Integer totalPage; // 总页数

    private Integer page; // 当前页

    private Integer offset; // 当前偏移量

    public static <T> BizPageResponse<T> success(T data, Long totalCount, int page) {
        return success(data, totalCount, page, 20);
    }

    public static <T> BizPageResponse<T> success(T data, Long totalCount, int page, int pageSize) {
        BizPageResponse<T> response = new BizPageResponse<T>();

        fillResponse(data, totalCount, page, pageSize, response);
        return response;
    }

    public static <T> BizPageResponse<T> success(T data, Integer totalCount, int page, int pageSize) {
        BizPageResponse<T> response = new BizPageResponse<T>();

        fillResponse(data, totalCount.longValue(), page, pageSize, response);
        return response;
    }

    public static <T> void fillResponse(T data, Long totalCount, int page, int pageSize, BizPageResponse<T> response) {
        pageSize = getPageSize(pageSize);
        int totalPage = getTotalPage((double) totalCount, pageSize);

        page = getPage(page, totalPage);

        int offset = getOffset(page, pageSize);

        response.setData(data);
        response.setStatus(SUCCESS_STATUS);
        response.setMessage(SUCCESS_MESSAGE);
        response.setTotal(totalCount);
        response.setPageSize(getPageSize(pageSize));
        response.setTotalPage(totalPage);
        response.setPage(page);
        response.setOffset(offset);
    }

    public static int getPageSize(int pageSize) {
        if (pageSize == 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    public static int getTotalPage(double totalCount, int pageSize) {
        int totalPage = (int) Math.ceil(totalCount / pageSize);
        return totalPage;
    }

    public static int getOffset(int page, int pageSize) {
        return pageSize * (page - 1);
    }

    public static int getPage(int page, int totalPage) {
        if (page > totalPage) {
            page = totalPage;
        }
        if (page <= 0) {
            page = 1;
        }
        return page;
    }

    public static <T> BizResponse<T> error(int responseStatus, String message, String moreInfo) {
        BizResponse<T> response = new BizResponse<>();
        response.setStatus(responseStatus);
        response.setMessage(message);
        return response;
    }

    public static <T> BizResponse<T> error(int responseStatus, String message) {
        return error(responseStatus, message, null);
    }

    public static <T> BizResponse<T> error(int responseStatus) {
        return error(responseStatus, null, null);
    }

    public static <T> BizPageResponse<T> errorList(int responseStatus) {
        BizPageResponse<T> res = new BizPageResponse<>();
        res.setStatus(responseStatus);
        return res;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
