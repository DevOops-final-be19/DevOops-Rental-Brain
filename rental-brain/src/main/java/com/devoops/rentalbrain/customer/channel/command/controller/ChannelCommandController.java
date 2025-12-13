package com.devoops.rentalbrain.customer.channel.command.controller;

import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandCreateDTO;
import com.devoops.rentalbrain.customer.channel.command.service.ChannelCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channel")
public class ChannelCommandController {
    private final ChannelCommandService channelCommandService;

    @Autowired
    public ChannelCommandController(ChannelCommandService channelCommandService) {
        this.channelCommandService = channelCommandService;
    }

    @PostMapping("/insert")
    public void insertChannel(@RequestBody ChannelCommandCreateDTO  channelCommandCreateDTO) {
        channelCommandService.insertChannel(channelCommandCreateDTO);
    }

}
