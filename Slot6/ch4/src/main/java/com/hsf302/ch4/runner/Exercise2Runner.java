package com.hsf302.ch4.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@RequiredArgsConstructor
public class Exercise2Runner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println(">>> Exercise 2 Runner");
    }
}