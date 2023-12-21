package com.coxey.app.model;

import com.coxey.app.annotations.Autowired;
import com.coxey.app.annotations.Component;

@Component("SUV")
public class SUV {
    @Autowired
    private Body body;
    @Autowired
    private Engine engine;

    @Override
    public String toString() {
        return "SUV{" +
                "body=" + body +
                ", engine=" + engine +
                '}';
    }
}
