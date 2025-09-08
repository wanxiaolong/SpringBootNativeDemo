package com.demo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.Executors;

@RestController
public class StreamResponseController {


  @GetMapping("/chat1")
  public ResponseEntity<StreamingResponseBody> chat1() {
    StreamingResponseBody body = outputStream -> {
      for (int i = 0; i < 10; i++) {
        String data = "data chunk " + i + "\n";
        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        sleep(500);
      }
    };
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
        .body(body);
  }

  @GetMapping("/chat2")
  public SseEmitter chat2() {
    SseEmitter emitter = new SseEmitter(60000L);
    Executors.newSingleThreadExecutor().submit(() -> {
      try {
        for (int i = 0; i < 10; i++) {
          emitter.send("message " + i);
          sleep(500);
        }
        emitter.complete();
      } catch (Exception e) {
        emitter.completeWithError(e);
      }
    });
    return emitter;
  }

  @GetMapping(value = "/chat3")
  public Flux<String> chat3() {
    return Flux.interval(Duration.ofMillis(500))
        .map(seq -> "flux " + seq + "<br>")
        .take(10); // 只返回前10个元素，然后流会自动结束。
  }

  private void sleep(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
