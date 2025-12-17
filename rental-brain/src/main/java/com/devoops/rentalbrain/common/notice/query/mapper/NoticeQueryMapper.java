package com.devoops.rentalbrain.common.notice.query.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeQueryMapper {
    List<Long> getEmployeeIds(Long id);
}
