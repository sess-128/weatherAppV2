    package com.rrtyui.weatherappv2.util.valid;

    import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
    import jakarta.validation.ConstraintValidator;
    import jakarta.validation.ConstraintValidatorContext;

    public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserSaveDto> {
        private String message;
        @Override
        public void initialize(PasswordMatches constraintAnnotation) {
            this.message = constraintAnnotation.message();
        }

        @Override
        public boolean isValid(UserSaveDto user, ConstraintValidatorContext context) {
            if (user.getPassword() == null || !user.getPassword().equals(user.getRepeatPassword())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode("repeatPassword")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
    }
