package com.martin.ELib.controller;

import com.martin.ELib.annotation.MethodDetails;
import com.martin.ELib.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;
	
    @RequestMapping("/say")
    @MethodDetails
    public String index(String whatDoyouWantToSay) {
        return "Greetings from Spring Boot: " + whatDoyouWantToSay;
    }

    @RequestMapping("/serviceSay")
    @MethodDetails
    public String serviceSay(String whatDoyouWantToSay) { return helloService.sayHello(whatDoyouWantToSay); }
}
