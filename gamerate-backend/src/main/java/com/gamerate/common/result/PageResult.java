package com.gamerate.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    private List<T> records;

    private Long total;

    private Long current;

    private Long size;

    private Long pages;

    public static <T> PageResult<T> empty(Long current, Long size) {
        return new PageResult<>(Collections.emptyList(), 0L, current, size, 0L);
    }

    public static <T> PageResult<T> of(List<T> records, Long total, Long current, Long size) {
        long pages = size == null || size == 0 ? 0 : (total + size - 1) / size;
        return new PageResult<>(records, total, current, size, pages);
    }
}
