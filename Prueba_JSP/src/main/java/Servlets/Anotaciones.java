package Servlets;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Anotaciones {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface PropertyName {
        String value();
    }

}
