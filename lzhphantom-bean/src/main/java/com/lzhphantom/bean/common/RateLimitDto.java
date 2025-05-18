package com.lzhphantom.bean.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class RateLimitDto implements Serializable {
    private Long id;
    private String key;
    private Integer maxRequests;
    private Long windowSize;
    private Boolean enabled;
}
