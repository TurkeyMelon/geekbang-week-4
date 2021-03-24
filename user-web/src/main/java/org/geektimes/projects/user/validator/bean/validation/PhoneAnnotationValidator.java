package org.geektimes.projects.user.validator.bean.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author Geoffrey
 */
public class PhoneAnnotationValidator implements ConstraintValidator<Phone, CharSequence> {

    private static final Pattern PHONE = Pattern.compile("(?:0|86|\\+86)?1[3-9]\\d{9}");

    private Pattern additionalPattern;

    @Override
    public void initialize(Phone phone) {
        this.additionalPattern = Pattern.compile(phone.regexp());
    }

    @Override
    public boolean isValid(CharSequence phone, ConstraintValidatorContext constraintValidatorContext) {
        return phone != null && PHONE.matcher(phone).matches() && additionalPattern.matcher(phone).matches();
    }
}
