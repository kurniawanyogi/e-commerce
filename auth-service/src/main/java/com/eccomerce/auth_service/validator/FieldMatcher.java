package com.eccomerce.auth_service.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Constraint(validatedBy = {FieldMatchValidator.class})
public @interface FieldMatcher {
    String message() default "firstField and secondField aren't same";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String firstField();

    String secondField();

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    @Constraint(validatedBy = {FieldMatchValidator.class})
    public @interface List {
        FieldMatcher[] value();
    }
}
