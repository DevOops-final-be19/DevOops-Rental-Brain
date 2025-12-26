package com.devoops.rentalbrain.common.ai.query.mapper;

import com.devoops.rentalbrain.common.ai.common.EmbeddingDTO;
import com.devoops.rentalbrain.common.ai.common.FeedBackEmbeddingDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmbeddingQueryMapper {

    List<FeedBackEmbeddingDTO> getFeedBacks();
}
