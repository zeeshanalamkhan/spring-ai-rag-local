# 📄 Spring Boot RAG with Ollama (PDF-based Java Q&A)

This project demonstrates a **Retrieval-Augmented Generation (RAG)** system built using **Spring Boot + Spring AI + Ollama**, designed to answer questions from a **local Java interview Q&A PDF**.

The application ingests a PDF, creates embeddings, stores them in a vector store, retrieves relevant context for a user question, and generates a grounded answer using a local LLM.

---

## 🚀 Features

* 📘 Ingest local PDF documents (Java interview Q&A)
* 🧠 Semantic search using embeddings
* 🤖 Local LLM via Ollama (no cloud dependency)
* 🔎 Context-aware answers (RAG)
* 🧾 Detailed logging for debugging and learning
* 🌱 Built with Spring Boot 3.x and Java 17

---

## 🧱 Architecture Overview

```
PDF → Chunking → Embeddings → Vector Store
                                 ↓
User Question → Similarity Search → Context → LLM → Answer
```

---

## 🛠️ Tech Stack

* **Java**: 17 (Amazon Corretto)
* **Spring Boot**: 3.2.x
* **Spring AI**: 0.8.1 (Milestone)
* **Ollama**: Local LLM runtime
* **LLM**: mistral (chat), nomic-embed-text (embeddings)
* **Vector Store**: SimpleVectorStore (in-memory)
* **PDF Parsing**: PagePdfDocumentReader (PDFBox)

---

## 📂 Project Structure

```
src/main/java
 └── com.zeeshan.ai
     ├── WeatherSpringAiApplication.java
     ├── controller
     │   └── AskController.java
     └── service
         ├── PdfIngestionService.java
         └── RagService.java

src/main/resources
 └── docs
     └── java-notes.pdf
```

---

## ⚙️ Prerequisites

1. **Java 17** installed
2. **Maven** (or use `mvnw.cmd`)
3. **Ollama** installed and running

Download models:

```bash
ollama pull mistral
ollama pull nomic-embed-text
```

Verify Ollama:

```bash
curl http://localhost:11434/api/tags
```

---

## 🔧 Configuration

### `application.yml`

```yaml
spring:
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: mistral
      embedding:
        model: nomic-embed-text

logging:
  level:
    com.zeeshan.ai: DEBUG
    org.springframework.ai: INFO
```

---

## 📘 PDF Ingestion

* Place your PDF inside:

  ```
  src/main/resources/docs/java-notes.pdf
  ```

* The PDF is automatically ingested at application startup

* Each page is converted into a `Document`

* Documents are embedded and stored in memory

Log example:

```
📄 Ingested 38 PDF pages into vector store
```

---

## ❓ Ask a Question

### Endpoint

```http
GET http://localhost:8080/ask?question=Function overloading v/s Function Overriding in Java
```

### Sample Response

```text
• Method Overloading allows multiple methods with the same name but different parameters.
• Method Overriding occurs when a subclass provides a specific implementation of a parent class method.
```

---

## 🧠 RAG Prompt Strategy

The system prompt enforces:

* Answer ONLY the user question
* Ignore unrelated context
* No hallucinated questions
* Concise, Java-focused explanations

Prompt size and context are logged for transparency.

---

## 🐢 Performance Notes

Initial startup may take ~1 minute due to:

* PDF parsing
* Embedding generation (CPU-based)

Subsequent queries:

* ~10–20 seconds (CPU-only)

### Optimizations Applied

* Top-K similarity search (K=3)
* Embedding model optimized for speed
* Context trimming

---

## ⚠️ Limitations

* Vector store is **in-memory** (lost on restart)
* No streaming responses
* CPU-only inference (no GPU)

---

## 🔮 Possible Enhancements

* Persist vector store (Redis / Chroma / FAISS)
* Streaming responses
* Source citation per answer
* Chunk-level relevance scoring
* Multi-PDF ingestion

---

## ✅ Status

✔ RAG pipeline working end-to-end
✔ Answers are grounded in PDF context
✔ Suitable as a learning and demo project

---

## 👨‍💻 Author

Built as a hands-on RAG POC using Spring Boot and local LLMs.

Happy hacking! 🚀
