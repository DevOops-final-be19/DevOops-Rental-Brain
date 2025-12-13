package com.devoops.rentalbrain.customer.channel.command.service;

import com.devoops.rentalbrain.customer.channel.command.dto.ChannelCommandCreateDTO;
import com.devoops.rentalbrain.customer.channel.command.entity.ChannelCommandEntity;
import com.devoops.rentalbrain.customer.channel.command.repository.ChannelRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelCommandServiceImpl implements ChannelCommandService {
    private final ModelMapper modelMapper;
    private final ChannelRepository channelRepository;


    @Autowired
    public ChannelCommandServiceImpl(ModelMapper modelMapper, ChannelRepository channelRepository) {
        this.modelMapper = modelMapper;
        this.channelRepository = channelRepository;
    }

    @Override
    @Transactional
    public void insertChannel(ChannelCommandCreateDTO channelCommandCreateDTO) {

        ChannelCommandEntity entity = new  ChannelCommandEntity();
        entity.setChannelName(channelCommandCreateDTO.getChannelName());

        channelRepository.save(entity);
    }
    
}
