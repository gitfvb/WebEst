package com.ibm.webest.persistence.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.ibm.webest.persistence.model.TemplateValues;

/**
 * Constraint annotation to validate that all phases of a {@link TemplateValues} object have unique numbers.
 * @see UniquePhaseNumbersValidator
 * @author Gregor Schumm
 *
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniquePhaseNumbersValidator.class)
public @interface UniquePhaseNumbers {
	/** The default message if the constraint is violated. **/
	String message() default "Phases must have unique Numbers.";

	/** The validation group this constraint belongs to. **/
    Class<?>[] groups() default {};

    /** Used to assign custom payload objects to the constraint. **/ 
    Class<? extends Payload>[] payload() default {};
}
