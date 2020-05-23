package com.graduation.mainapp.validation;

import com.graduation.mainapp.validation.annotation.PasswordsMatchValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Objects.isNull;

@Slf4j
public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatchValidation, Object> {

    private String password;
    private String confirmPassword;

    @Override
    public void initialize(PasswordsMatchValidation passwordsMatchValidation) {
        this.password = passwordsMatchValidation.password();
        this.confirmPassword = passwordsMatchValidation.confirmPassword();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(password);
        Object confirmPasswordValue = new BeanWrapperImpl(value).getPropertyValue(confirmPassword);

        if (isNull(passwordValue) || isNull(confirmPasswordValue)) {
            return false;
        }

        String password = (String) passwordValue;
        String confirmPassword = (String) confirmPasswordValue;

        return password.equals(confirmPassword);
    }
}
