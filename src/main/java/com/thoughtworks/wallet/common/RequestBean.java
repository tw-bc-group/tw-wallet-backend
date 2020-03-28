package com.thoughtworks.wallet.common;

import lombok.Data;

//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class RequestBean {

    /**
     * 返回的数据
     */
    private Object data;

    public RequestBean(Object requestBean) {

        this.data = requestBean;
    }

    public RequestBean() {

    }
}
