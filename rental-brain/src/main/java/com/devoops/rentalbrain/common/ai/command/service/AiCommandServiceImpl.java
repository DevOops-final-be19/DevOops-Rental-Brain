package com.devoops.rentalbrain.common.ai.command.service;

import com.devoops.rentalbrain.common.ai.command.dto.KeywordCountDTO;
import com.devoops.rentalbrain.common.ai.command.dto.MetaDataDTO;
import com.devoops.rentalbrain.common.ai.common.EmbeddingDTO;
import com.devoops.rentalbrain.common.ai.command.repository.OpenSearchVectorRepository;
import com.devoops.rentalbrain.common.ai.common.SentimentDTO;
import com.devoops.rentalbrain.common.ai.query.service.AiQueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.embeddings.CreateEmbeddingResponse;
import com.openai.models.embeddings.EmbeddingCreateParams;
import com.openai.models.embeddings.EmbeddingModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseOutputItem;
import com.openai.models.responses.ResponseOutputText;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiCommandServiceImpl implements AiCommandService {
    private final OpenAIClient openAIClient;
    private final OpenSearchVectorRepository openSearchVectorRepository;
    private final AiQueryService aiQueryService;

    public AiCommandServiceImpl(OpenAIClient openAIClient,
                                OpenSearchVectorRepository openSearchVectorRepository,
                                AiQueryService aiQueryService) {
        this.openAIClient = openAIClient;
        this.openSearchVectorRepository = openSearchVectorRepository;
        this.aiQueryService = aiQueryService;
    }

    public List<Float> embed(String input) {
        EmbeddingCreateParams params = EmbeddingCreateParams.builder()
                .model(EmbeddingModel.TEXT_EMBEDDING_3_SMALL) // 1536-d :contentReference[oaicite:7]{index=7}
                .input(input)
                .build();

        CreateEmbeddingResponse res = openAIClient.embeddings().create(params);
        log.info("Embedding created: {}", res);
        // 첫 번째 벡터만 사용(단일 input)
        return res.data().get(0).embedding();
    }

    @Transactional(readOnly = true)
    public void indexDocument() throws IOException {
        for (EmbeddingDTO embeddingDTO : aiQueryService.getFeedBacks()) {
            String prompt = buildVocabSentimentPrompt(embeddingDTO.getText());

            Response response = openAIClient.responses().create(
                    ResponseCreateParams.builder()
                            .model(ChatModel.GPT_5_1)
                            .input(prompt)
                            .temperature(0)
                            .build()
            );

            String outputText = response.output().stream()
                    .flatMap(item -> item.message().stream())
                    .flatMap(message -> message.content().stream())
                    .flatMap(content -> content.outputText().stream())
                    .map(ResponseOutputText::text)
                    .reduce("", (a, b) -> a + b);
            int start = outputText.indexOf('{');
            int end = outputText.lastIndexOf('}');
            if (start < 0 || end < 0 || start >= end) {
                throw new IllegalArgumentException("Invalid JSON: " + outputText);
            }
            ObjectMapper mapper = new ObjectMapper();

            SentimentDTO sentimentDTO = mapper.readValue(outputText, SentimentDTO.class);

            List<Float> vector = embed(embeddingDTO.getText());

            Map<String, Object> doc = new HashMap<>();
            doc.put("chunkId", embeddingDTO.getChunkId());
            doc.put("text", embeddingDTO.getText());
            doc.put("embedding", vector);
            doc.put("source", embeddingDTO.getSource());
            doc.put("sourceId", embeddingDTO.getSourceId());
            doc.put("customerId", embeddingDTO.getCustomerId());
            doc.put("segments", embeddingDTO.getSegments());
            doc.put("sentiment", sentimentDTO.getSentiment());
            doc.put("score", embeddingDTO.getScore());
            doc.put("category", embeddingDTO.getCategory());
            doc.put("priority", embeddingDTO.getPriority());
            doc.put("status", embeddingDTO.getStatus());
            doc.put("vocab", sentimentDTO.getVocab());
            doc.put("createAt", embeddingDTO.getCreateAt());
            log.info("indexOneDocument: {}", doc);

            openSearchVectorRepository.upsertChunk(embeddingDTO.getChunkId(), doc);
        }
    }

    private String buildVocabSentimentPrompt(String text) {
        return """
                당신은 고객 피드백 분석 엔진입니다.
                
                아래 텍스트에서 "감정"과 "이슈를 나타내는 핵심 구(phrase)"만 추출해서 JSON 형식으로 반환하세요.
                
                규칙:
                - 2단어 이상으로 의미가 완성되는 표현은 유지
                  (예: 제품 불량, 서비스 질 저하)
                - 감정 강도 표현은 키워드에서 제외
                  (예: 매우 만족, 매우 빠름, 아주 좋음, 매우 불만, 매우 느림, 아주 나쁨)
                - 강조어(매우, 아주, 상당히)는 제거
                - 이슈의 대상 + 상태가 명확한 표현만 유지
                  (예: 응대 속도, 서비스 품질 저하, 제품 불량)
                - 단독으로 의미가 약한 일반 단어는 제외
                  (예: 제품, 서비스, 만족)
                - 불용어가 포함되더라도 전체가 이슈라면 유지
                - 명사/형용사 중심
                - 최대 6개
                - JSON 외 출력 금지
                - 중복 단어 제거
                - 감정은 문맥 기준으로 판단
                
                출력 형식:
                {
                  "vocab": [],
                  "sentiment": "긍정 | 중립 | 부정"
                }
                
                텍스트:
                \"\"\"
                %s
                \"\"\"
                """.formatted(text);
    }


    //    public List<String> retrieveTopK(String question, int k) throws IOException {
//        List<Float> qVec = embed(question);
//        SearchResponse<Map> res = openSearchVectorRepository.knnSearch(qVec, k);
//
//        List<String> texts = new ArrayList<>();
//        res.hits().hits().forEach(hit -> {
//            Map src = hit.source();
//            if (src != null && src.get("text") != null) texts.add(String.valueOf(src.get("text")));
//        });
//        return texts;
//    }
    public List<String> retrieveTopK(String question, Map<String, Object> filter, int k) throws IOException {
        List<Float> qVec = embed(question);

        float[] arr = new float[qVec.size()];
        for (int i = 0; i < qVec.size(); i++) {
            arr[i] = qVec.get(i);
        }

        SearchResponse<Map> res =
                openSearchVectorRepository.knnSearchWithFilter(arr, k, filter);

        return res.hits().hits().stream()
                .map(Hit::source)
                .filter(src -> src != null && src.get("text") != null)
                .map(src -> String.valueOf(src.get("text")))
                .distinct()
                .toList();
    }


    public Response answer(String question) throws IOException {

        MetaDataDTO meta = extract(question);
        log.info("meta: {}", meta);
        Map<String, Object> filter = meta.toFilterMap();

        List<String> contexts = retrieveTopK(question,filter, 10);

        if (contexts.isEmpty()) {
            return openAIClient.responses().create(
                    ResponseCreateParams.builder()
                            .model(ChatModel.GPT_5_1)
                            .input("문서에서 근거를 찾지 못했습니다.")
                            .build()
            );
        }

        String input = buildPrompt(contexts,question,meta.getResponseStyle());

        log.info("input: {}", input);

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_1)
                .input(input)
                .temperature(0.2)
                .build();

        return openAIClient.responses().create(params);
    }

    private String buildPrompt(List<String> ctx, String q, String style) {
        String rule = switch (style) {
            case "summary" -> "핵심 이슈 위주로 요약하라.";
            case "list" -> "항목별로 정리하라.";
            default -> "이유를 설명하라.";
        };

        return """
        SYSTEM:
        너는 회사 내부 문서 기반 Q&A 어시스턴트다.
        아래 CONTEXT 범위 안에서만 답변하고, 근거가 없으면 "문서에서 근거를 찾지 못했습니다"라고 말해라.

        RULE:
        %s

        CONTEXT:
        %s

        QUESTION:
        %s
        """.formatted(rule, String.join("\n---\n", ctx), q);
    }

    public List<KeywordCountDTO> getTop3NegativeKeywords() throws IOException {
        return openSearchVectorRepository.getTopKeywords("부정", 3);
    }

    public List<KeywordCountDTO> getTop3PositiveKeywords() throws IOException {
        return openSearchVectorRepository.getTopKeywords("긍정", 3);
    }

    public MetaDataDTO extract(String question) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        Response response = openAIClient.responses().create(
                ResponseCreateParams.builder()
                        .model(ChatModel.GPT_5_1)
                        .input(buildQueryMetadataPrompt(question))
                        .temperature(0)
                        .build()
        );

        String json = response.output().stream()
                .flatMap(o -> o.message().stream())
                .flatMap(m -> m.content().stream())
                .flatMap(c -> c.outputText().stream())
                .map(ResponseOutputText::text)
                .reduce("", String::concat);

        return objectMapper.readValue(json, MetaDataDTO.class);
    }

    private String buildQueryMetadataPrompt(String question) {
        return """
        너는 검색용 질문 분석기다.
        질문에서 검색 조건과 응답 방식을 분리해 JSON으로 반환하라.

        규칙:
        - JSON 외 텍스트 금지
        - responseStyle:
          - 요약, 정리 → summary
          - 목록, 나열 → list
          - 이유, 왜 → explain

        출력 형식:
        {
          "category": null | "서비스 만족" | "제품 불량" | "제품 품질" | "AS 지연" | "직원 응대" | "서비스 불만"
          "sentiment": null | "긍정" | "부정" | "중립",
          "vocab": [],
          "segments": null | string,
          "responseStyle": "summary | list | explain"
        }

        질문:
        \"\"\"%s\"\"\"
        """.formatted(question);
    }

}
