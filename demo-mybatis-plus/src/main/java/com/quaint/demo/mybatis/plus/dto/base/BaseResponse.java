package com.quaint.demo.mybatis.plus.dto.base;

import lombok.Data;

/**
 * @author qi cong
 * @date 2019-12-25 16:55
 */
@Data
public class BaseResponse<T> {

    private T body;

    private String code;

    private String message;

    private String totalCount;

    public BaseResponse() {
    }

    public BaseResponse(T body) {
        this.body = body;
    }

    public BaseResponse(T body, String totalCount) {
        this.body = body;
        this.totalCount = totalCount;
    }
}
