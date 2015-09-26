package com.ibm.webest.persistence.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.openjpa.persistence.jdbc.ForeignKey;
import org.apache.openjpa.persistence.jdbc.ForeignKeyAction;

import com.ibm.webest.persistence.validation.ValidPhasePercentages;

/**
 * Entity implementation class for Entity: ProjectEnvironment<br>
 * 
 * @author Gregor Schumm
 */
@Entity
@ValidPhasePercentages
public class ProjectEnvironment extends TemplateValues implements Serializable {
	@OneToOne(optional = false, cascade={ CascadeType.MERGE})
	@JoinColumn(name = "estimate", unique = true, nullable = false)
	@ForeignKey(deleteAction=ForeignKeyAction.CASCADE)
	private Estimate estimate;
	@NotNull
	@Size(min=1, max=50)
	@Column(length = 50, nullable = false)
	private String projectName;
	@Size(max=4000)
	@Column(length = 4000, nullable = true)
	private String projectDescription;
	@NotNull
	@ManyToOne(optional = false)
	@ForeignKey
	@JoinColumn(name = "mttdtimeunit", nullable = false, referencedColumnName = "id")
	private MTTDTimeUnit mttdTimeUnit;
	@NotNull
	@ManyToOne(optional = false)
	@ForeignKey
	@JoinColumn(name = "effortunit", nullable = false, referencedColumnName = "id")
	private EffortUnit effortUnit;
	@NotNull
	@ManyToOne(optional = false)
	@ForeignKey
	@JoinColumn(name = "monetaryunit", nullable = false, referencedColumnName = "id")
	private MonetaryUnit monetaryUnit;
	@NotNull
	@ManyToOne(optional = false)
	@ForeignKey
	@JoinColumn(name = "applicationtype", nullable = false, referencedColumnName = "id")
	private ApplicationType applicationType;
	@NotNull
	@ManyToOne(optional = false)
	@ForeignKey
	@JoinColumn(name = "industrysector", nullable = false, referencedColumnName = "id")
	private IndustrySector industrySector;
	@NotNull
	@ManyToOne(optional = false)
	@ForeignKey
	@JoinColumn(name = "country", nullable = false, referencedColumnName = "code")
	private Country country;
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@JoinColumn(name = "template", nullable = true)
	private Template template;

	public ProjectEnvironment() {
		super();
	}

	/**
	 * @return the estimate, this project environment belongs to
	 */
	@XmlTransient
	public Estimate getEstimate() {
		return this.estimate;
	}

	/**
	 * @param estimate
	 *            the estimate, this project environment belongs to
	 */
	public void setEstimate(Estimate estimate) {
		this.estimate = estimate;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @return the name of the project
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * Required and may have max. 50 characters.
	 * 
	 * @param projectName
	 *            the name of the project
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @return a description of the project
	 */
	public String getProjectDescription() {
		return this.projectDescription;
	}

	/**
	 * May have max. 4000 characters.
	 * 
	 * @param projectDescription
	 *            a description of the project
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/**
	 * Required.
	 * 
	 * @return the used mean time to defect time unit
	 */
	public MTTDTimeUnit getMttdTimeUnit() {
		return mttdTimeUnit;
	}

	/**
	 * Required.
	 * 
	 * @param mttdTimeUnit
	 *            the used mean time to defect time unit
	 */
	public void setMttdTimeUnit(MTTDTimeUnit mttdTimeUnit) {
		this.mttdTimeUnit = mttdTimeUnit;
	}

	/**
	 * Required.
	 * 
	 * @return the used effort unit
	 */
	public EffortUnit getEffortUnit() {
		return effortUnit;
	}

	/**
	 * Required.
	 * 
	 * @param effortUnit
	 *            the used effort unit
	 */
	public void setEffortUnit(EffortUnit effortUnit) {
		this.effortUnit = effortUnit;
	}

	/**
	 * Required.
	 * 
	 * @return the used monetary unit
	 */
	public MonetaryUnit getMonetaryUnit() {
		return monetaryUnit;
	}

	/**
	 * Required.
	 * 
	 * @param monetaryUnit
	 *            the used monetary unit
	 */
	public void setMonetaryUnit(MonetaryUnit monetaryUnit) {
		this.monetaryUnit = monetaryUnit;
	}

	/**
	 * Required.
	 * 
	 * @return the application type of the project
	 */
	public ApplicationType getApplicationType() {
		return applicationType;
	}

	/**
	 * Required.
	 * 
	 * @param applicationType
	 *            the application type of the project
	 */
	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	/**
	 * Required.
	 * 
	 * @return the affected industry sector
	 */
	public IndustrySector getIndustrySector() {
		return industrySector;
	}

	/**
	 * Required.
	 * 
	 * @param industrySector
	 *            the affected industry sector
	 */
	public void setIndustrySector(IndustrySector industrySector) {
		this.industrySector = industrySector;
	}

	/**
	 * Required.
	 * 
	 * @return the country, the project is carried out
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * Required.
	 * 
	 * @param country
	 *            the country, the project is carried out
	 */
	public void setCountry(Country country) {
		this.country = country;
	}

	/**
	 * Optional.
	 * 
	 * @param template
	 *            the template used by this project environment
	 */
	public void setTemplate(Template template) {
		this.template = template;
	}

	/**
	 * @return the template used by this project environment
	 */
	public Template getTemplate() {
		return template;
	}

}
