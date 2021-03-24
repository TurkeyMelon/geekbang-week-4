package org.geektimes.projects.user.validator.bean.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Geoffrey
 */
@Documented
@Constraint(validatedBy = {PhoneAnnotationValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(Phone.List.class)
public @interface Phone {

    String message() default "手机格式不正确";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String regexp() default ".*";

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Phone[] value();
    }
}
