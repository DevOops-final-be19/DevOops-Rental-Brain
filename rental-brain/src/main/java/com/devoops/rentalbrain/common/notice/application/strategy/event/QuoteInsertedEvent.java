package com.devoops.rentalbrain.common.notice.application.strategy.event;

import com.devoops.rentalbrain.common.notice.application.domain.PositionType;

public record QuoteInsertedEvent (
        PositionType positionId
) implements NotificationEvent {}
