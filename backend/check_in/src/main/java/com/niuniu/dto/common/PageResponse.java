package com.niuniu.dto.common;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> list;
    private long total;
    private Integer pageNum;
    private Integer pageSize;
}
