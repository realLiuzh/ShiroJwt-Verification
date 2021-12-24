package com.lzh.model.po;

import java.io.Serializable;
import lombok.Data;

/**
 * permission
 * @author 
 */
@Data
public class Permission implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 权限代码字符串
     */
    private String perCode;

    /**
     * 资源名称
     */
    private String comment;

    private static final long serialVersionUID = 1L;
}