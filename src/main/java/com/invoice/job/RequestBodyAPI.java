package com.invoice.job;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// POJO for the request body
@Setter
@Getter
public class RequestBodyAPI {
    private List<Integer> ids;

    public RequestBodyAPI(List<Integer> ids) {
        this.ids = ids;
    }

}
