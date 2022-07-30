package com.oowanghan.atlantis.framework.mysql.mybatis;

import cn.hutool.core.util.StrUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @Author WangHan
 * @Create 2020/9/17 4:29 下午
 */
public class BaseRepository {

    public Pageable makePageable(int page, int pageSize, String sortField, String sortType) {

        if (StrUtil.isBlank(sortType) || StrUtil.isBlank(sortField)) {
            return PageRequest.of(Math.max(page - 1, 0), pageSize);
        } else {
            Sort sort = Sort.by(Sort.Direction.fromString(sortType), sortField);
            return PageRequest.of(Math.max(page - 1, 0), pageSize, sort);
        }
    }
}
