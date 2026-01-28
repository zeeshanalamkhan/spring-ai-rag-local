package com.zeeshan.ai.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfIngestionService {

    private static final Logger log =
            LoggerFactory.getLogger(PdfIngestionService.class);

    private final SimpleVectorStore vectorStore;

    public PdfIngestionService(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void ingest() {

        try {
            PagePdfDocumentReader reader =
                    new PagePdfDocumentReader(new ClassPathResource("docs/notes.pdf"));

            List<Document> documents = reader.get();
            vectorStore.add(documents);

            log.info("📄 Ingested {} PDF pages into vector store", documents.size());
        } catch (Exception e) {
            log.warn("⚠️ No PDF ingested: {}", e.getMessage());
        }
    }
}
