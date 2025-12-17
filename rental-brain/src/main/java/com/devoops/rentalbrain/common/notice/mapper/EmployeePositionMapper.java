package com.devoops.rentalbrain.common.notice.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeePositionMapper {
    List<Long> getEmployeeIds(Long id);
}
