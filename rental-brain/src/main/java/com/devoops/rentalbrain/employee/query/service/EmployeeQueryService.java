package com.devoops.rentalbrain.employee.query.service;

import com.devoops.rentalbrain.employee.query.mapper.EmployeeQueryMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeQueryService {
    private final EmployeeQueryMapper employeeQueryMapper;

    public EmployeeQueryService(EmployeeQueryMapper employeeQueryMapper) {
        this.employeeQueryMapper = employeeQueryMapper;
    }

    public List<GrantedAuthority> getUserAuth(Long empId, Long positionId){
        return employeeQueryMapper.getUserAuth(empId,positionId).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
