package com.devoops.rentalbrain.common.ai.command.service;

import com.devoops.rentalbrain.common.ai.common.EmbeddingDTO;
import com.openai.models.responses.Response;

import java.io.IOException;
import java.util.List;

public interface AiCommandService {
    List<Float> embed(String input);

    void indexOneDocument() throws IOException;

    Response answer(String q) throws IOException;
}
