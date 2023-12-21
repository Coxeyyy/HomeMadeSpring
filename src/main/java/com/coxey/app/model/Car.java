package com.coxey.app.model;

import com.coxey.app.annotations.Autowired;
import com.coxey.app.annotations.Component;

@Component("Car")
public class Car {
    private Body body;
    private Engine engine;

    @Autowired
    public Car(Body body, Engine engine) {
        this.body = body;
        this.engine = engine;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return "Car{" +
                "body=" + body +
                ", engine=" + engine +
                '}';
    }
}