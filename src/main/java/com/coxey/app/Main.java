package com.coxey.app;

import com.coxey.app.framework.Context;
import com.coxey.app.model.Bus;
import com.coxey.app.model.Car;
import com.coxey.app.model.SUV;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Context context = Context.load("com.coxey.app.model");
        System.out.println("Все классы присутствующие в пакете com.coxey.app.model: ");
        System.out.println(context.getLoadedClasses());
        System.out.println();

        System.out.println("Создаем класс Car у которого конструктор @Autowired: ");
        Car car = (Car) context.get("Car");
        System.out.println(car.toString());
        System.out.println();

        System.out.println("Создаем класс SUV у которого все поля @Autowired");
        SUV suv = (SUV) context.get("SUV");
        System.out.println(suv.toString());
        System.out.println();

        System.out.println("Создаем класс Bus у которого 2 поля @Autowired, 1 поле без @Autowired: ");
        Bus bus = (Bus) context.get("Bus");
        System.out.println(bus.toString());
    }
}