package com.invoice.job;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class RequestBodyAPI {
    // Getters and setters
    private List<Integer> ids;

    // Constructor
    public RequestBodyAPI(List<Integer> ids) {
        this.ids = ids;
    }

}