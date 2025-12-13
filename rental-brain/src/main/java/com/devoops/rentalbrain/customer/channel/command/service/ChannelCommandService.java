package com.devoops.rentalbrain.customer.channel.command.service;

import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandCreateDTO;

public interface ChannelCommandService {
    void insertChannel(ChannelCommandCreateDTO channelCommandCreateDTO);
}
