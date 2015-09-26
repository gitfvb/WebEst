package com.ibm.webest.persistence.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Constraint annotation to validate that at least one use case of a solution is in scope.
 * @see OneUseCaseInScopeValidator
 * @author Gregor Schumm
 *
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OneUseCaseInScopeValidator.class)
public @interface OneUseCaseInScope {
	/** The default message if the constraint is violated. **/
	String message() default "At least one use case has to be in scope.";

	/** The validation group this constraint belongs to. **/
    Class<?>[] groups() default {};

    /** Used to assign custom payload objects to the constraint. **/ 
    Class<? extends Payload>[] payload() default {};
}
