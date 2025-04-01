package com.rrtyui.weatherappv2.util.valid;

import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserSaveDto> {
    @Override
    public boolean isValid(UserSaveDto user, ConstraintValidatorContext context) {
        if (user.getPassword() == null || !user.getPassword().equals(user.getRepeatPassword())) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("repeatPassword")
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
