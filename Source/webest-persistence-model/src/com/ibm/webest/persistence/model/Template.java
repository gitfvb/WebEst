package com.ibm.webest.persistence.model;

import java.io.Serializable;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entity implementation class for Entity: Template<br>
 * Predefined values for the project environment.
 * 
 * @see ProjectEnvironment
 * @see TemplateValues
 * @author Gregor Schumm
 */
@Entity
public class Template extends TemplateValues implements Serializable {
	@NotNull
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String name;
	@Size(max = 4000)
	@Column(length = 4000, nullable = true)
	private String description;
	@OneToMany(mappedBy = "template")
	private Set<ProjectEnvironment> projectEnvironments;
	private static final long serialVersionUID = 1L;

	public Template() {
		super();
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @return the name of the template
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @param name
	 *            the name of the template
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @return a description for the template
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @param description
	 *            a description for the template
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param projectEnvironments
	 *            project environments that should use this template
	 */
	public void setProjectEnvironments(
			Set<ProjectEnvironment> projectEnvironments) {
		this.projectEnvironments = projectEnvironments;
	}

	/**
	 * @return project environments that uses this template
	 */
	@XmlTransient
	public Set<ProjectEnvironment> getProjectEnvironments() {
		return projectEnvironments;
	}

}
