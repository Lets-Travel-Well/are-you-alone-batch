package com.rualone.app.main.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Slf4j
public class ValidateUtil {

    private final Validator validator;

    public boolean validate(Object entity) {
        Set<ConstraintViolation<Object>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Object> violation : violations) {
//                log.warn(violation.getMessage());
                return false;
            }
        }

        return true;
    }
}
