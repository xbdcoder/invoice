package com.invoice.job;

import java.util.List;

public class RequestBodyAPI {
    private List<Integer> ids;

    // Constructor
    public RequestBodyAPI(List<Integer> ids) {
        this.ids = ids;
    }

    // Getters and setters
    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}