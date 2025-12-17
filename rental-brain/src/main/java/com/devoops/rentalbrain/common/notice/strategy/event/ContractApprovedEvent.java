package com.devoops.rentalbrain.common.notice.strategy.event;

public record ContractApprovedEvent(
        Long EmpId
) implements NotificationEvent {}
