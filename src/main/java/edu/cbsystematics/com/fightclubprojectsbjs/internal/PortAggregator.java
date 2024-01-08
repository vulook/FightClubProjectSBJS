package edu.cbsystematics.com.fightclubprojectsbjs.internal;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PortAggregator {

    @Value("${server.port}")
    private int serverPort;


    @Value("${server.anotherPort}")
    private int anotherPort;

}