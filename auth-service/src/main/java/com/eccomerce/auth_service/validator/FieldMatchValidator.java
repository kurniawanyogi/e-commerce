package com.eccomerce.auth_service.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class FieldMatchValidator implements ConstraintValidator<FieldMatcher, Object> {

    private String firstField;
    private String secondField;
    private String message;

    @Override
    public void initialize(final FieldMatcher constraintAnnotation) {
        firstField = constraintAnnotation.firstField();
        secondField = constraintAnnotation.secondField();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object object, final ConstraintValidatorContext constraintValidatorContext) {
        boolean valid;

        try {
            final Object field1 = org.apache.commons.beanutils.BeanUtils.getProperty(object, firstField);

            final Object field2 = BeanUtils.getProperty(object, secondField);

            valid = field1 == null && field2 == null || field1 != null && field1.equals(field2);

        } catch (Exception e) {
            log.error(String.format("cannot validate field in %s", object));
            return false;
        }

        if (!valid) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(firstField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}

