package com.graduation.mainapp.validation.annotation;

import com.graduation.mainapp.validation.PasswordsMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordsMatchValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordsMatchValidation {

    String message() default "New password must match with confirm password";

    String password();

    String confirmPassword();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
