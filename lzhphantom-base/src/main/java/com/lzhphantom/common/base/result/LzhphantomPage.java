package com.lzhphantom.common.base.result;

import lombok.Data;

import java.util.List;

@Data
public class LzhphantomPage<T> {
    private Long total;
    private List<T> records;
}
