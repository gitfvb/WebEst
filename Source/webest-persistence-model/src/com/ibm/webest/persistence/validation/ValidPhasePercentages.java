package com.ibm.webest.persistence.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.ibm.webest.persistence.model.TemplateValues;

/**
 * Constraint annotation to validate that the sum of all phase percentages of the {@link TemplateValues}' associated phases equal 100%.
 * @see ValidPhasePercentagesValidator
 * @author Gregor Schumm
 *
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPhasePercentagesValidator.class)
public @interface ValidPhasePercentages {
	/** The default message if the constraint is violated. **/
	String message() default "The overall percentage of all phases must equal 100%.";

	/** The validation group this constraint belongs to. **/
    Class<?>[] groups() default {};

    /** Used to assign custom payload objects to the constraint. **/ 
    Class<? extends Payload>[] payload() default {};
}
