package com.lzh.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lzh.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private Integer code;
    private String message;
    private Object data;

    public static Response success(Object data) {
        Response response = new Response();
        response.setCode(200);
        response.setMessage("操作成功");
        response.setData(data);
        return response;
    }

    public static Response success() {
        Response response = new Response();
        response.setCode(200);
        response.setMessage("操作成功");
        return response;
    }


    public static Response error(CustomException e) {
        Response response = new Response();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return response;
    }
}
