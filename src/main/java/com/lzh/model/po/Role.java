package com.lzh.model.po;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * role
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {
    /**
     * ID
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色备注
     */
    private String comment;

    private static final long serialVersionUID = 1L;
}