package com.zeeshan.ai.config;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    @Bean
    SimpleVectorStore vectorStore(EmbeddingClient embeddingClient){
        return new SimpleVectorStore(embeddingClient);
    }
}
