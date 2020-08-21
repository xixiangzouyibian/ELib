package com.martin.ELib.service;

import com.martin.ELib.annotation.MethodDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    @MethodDetails
    public String sayHello(String message) {
        try {
            // mock other business logic doing
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Hello Service is saying: " + message;
    }
}
