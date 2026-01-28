package com.zeeshan.ai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class RagService {

    private static final Logger log =
            LoggerFactory.getLogger(RagService.class);

    private final ChatClient chatClient;
    private final SimpleVectorStore vectorStore;

    public RagService(ChatClient chatClient, SimpleVectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String ask(String question) {
        Instant start = Instant.now();
        log.info("➡️ Question received: {}", question);

        // 1️⃣ Retrieve context (optional but safe)
        String context = vectorStore.similaritySearch(
                        SearchRequest.query(question).withTopK(3)
                                .withSimilarityThreshold(0.75)
                ).stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        log.debug("📚 Retrieved context:\n{}", context);

        // 2️⃣ CLEAN PROMPT (NO system/user roles)
        String promptText = """
                You are a senior Java engineer.
                
                Answer the question clearly and concisely.
                Use bullet points and Java examples where applicable.
                
                Context (may be empty):
                %s
                
                Question:
                %s
                """.formatted(context, question);

        log.info("🧾 Prompt length: {}", promptText.length());
        log.debug("🧾 Prompt sent to LLM:\n{}", promptText);

        // 3️⃣ Call Ollama
        String response = chatClient
                .call(new Prompt(promptText))
                .getResult()
                .getOutput()
                .getContent();

        Duration duration = Duration.between(start, Instant.now());
        log.info("✅ Response generated in {} seconds", duration.toSeconds());

        return response;
    }
}
