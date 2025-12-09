package com.devoops.rentalbrain.employee.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Mapper
public interface EmployeeQueryMapper {
    List<String> getUserAuth(Long empId, Long positionId);
}
