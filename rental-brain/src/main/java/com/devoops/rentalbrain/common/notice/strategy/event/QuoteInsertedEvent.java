package com.devoops.rentalbrain.common.notice.strategy.event;

import com.devoops.rentalbrain.common.notice.domain.PositionType;

public record QuoteInsertedEvent (
        PositionType positionId
) implements NotificationEvent {}
