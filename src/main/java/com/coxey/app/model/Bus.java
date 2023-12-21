package com.coxey.app.model;

import com.coxey.app.annotations.Autowired;
import com.coxey.app.annotations.Component;

@Component("Bus")
public class Bus {
    @Autowired
    private Engine engine;
    @Autowired
    private Body body;

    private Driver driver;

    @Override
    public String toString() {
        return "Bus{" +
                "engine=" + engine +
                ", body=" + body +
                ", driver=" + driver +
                '}';
    }
}