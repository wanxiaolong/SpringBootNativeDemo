package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  @RequestMapping("/")
  String home() throws InterruptedException {
    Thread.sleep(10000);
    return "Hello World!";
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
