package com.ibm.webest.persistence.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Constraint annotation to validate that at least one of either PP or PI is specified in a solution.
 * @see ValidPiPpSelection
 * @author Gregor Schumm
 *
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPiPpSelectionValidator.class)
public @interface ValidPiPpSelection {
	/** The default message if the constraint is violated. **/
	String message() default "You have to specify either a PI or PP value. If both are given, PP is used.";

	/** The validation group this constraint belongs to. **/
    Class<?>[] groups() default {};

    /** Used to assign custom payload objects to the constraint. **/ 
    Class<? extends Payload>[] payload() default {};
}
