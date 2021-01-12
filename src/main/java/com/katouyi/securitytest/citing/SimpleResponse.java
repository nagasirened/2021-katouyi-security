package com.katouyi.securitytest.citing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sucurity-demo
 * @description: SimpleResponse
 * @author: ZengGuangfu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleResponse {
    private Integer code;
    private String msg;
    private Object content;
}
