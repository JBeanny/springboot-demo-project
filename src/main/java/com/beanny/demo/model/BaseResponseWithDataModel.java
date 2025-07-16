package com.beanny.demo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponseWithDataModel extends BaseResponseModel {
    private Object data;
    
    public BaseResponseWithDataModel(String status, String message, Object data) {
        super(status, message);
        this.data = data;
    }
}
