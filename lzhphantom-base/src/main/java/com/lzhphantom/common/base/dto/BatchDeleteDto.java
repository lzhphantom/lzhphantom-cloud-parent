package com.lzhphantom.common.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchDeleteDto<T> implements Serializable {
    private List<T> ids;
}
