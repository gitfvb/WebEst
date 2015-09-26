package com.ibm.webest.persistence.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.ibm.webest.persistence.model.TemplateValues;

/**
 * Constraint annotation to validate that all milestones of a {@link TemplateValues} object have unique milestone IDs.
 * @see UniqueMilestoneIdsValidator
 * @author Gregor Schumm
 *
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueMilestoneIdsValidator.class)
public @interface UniqueMilestoneIds {
	/** The default message if the constraint is violated. **/
	String message() default "Milestones must have unique IDs.";
	
	/** The validation group this constraint belongs to. **/
    Class<?>[] groups() default {};
    
    /** Used to assign custom payload objects to the constraint. **/
    Class<? extends Payload>[] payload() default {};
}
