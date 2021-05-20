package com.shanwije.backpacker.config;

import com.shanwije.backpacker.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

@Configuration
@AllArgsConstructor
@Log4j2
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        System.out.println("hello world");
    }
}
