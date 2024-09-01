package be.jimsa.iotproject.config.validation;


import be.jimsa.iotproject.config.validation.annotation.ValidPublicId;
import be.jimsa.iotproject.utility.constant.ProjectConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PublicIdValidator implements ConstraintValidator<ValidPublicId, String> {

    @Override
    public void initialize(ValidPublicId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String publicId, ConstraintValidatorContext context) {
        if (publicId == null) {
            return false;
        }
        return publicId.matches(ProjectConstants.VALIDATION_PUBLIC_ID_PATTERN) &&
                publicId.length() >= ProjectConstants.VALIDATION_PUBLIC_ID_MIN &&
                publicId.length() <= ProjectConstants.VALIDATION_PUBLIC_ID_MAX;
    }


}
