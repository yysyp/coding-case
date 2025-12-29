package com.poc.mpt.error;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExceptionMapping {

    String code() default "4000";

    int httpStatus() default 400;

    String msg() default "Your error message!";

    /**
     * only the msgReplaceable is true and message is not null, the message will replace the msg
     */
    boolean msgReplaceable() default false;

}
