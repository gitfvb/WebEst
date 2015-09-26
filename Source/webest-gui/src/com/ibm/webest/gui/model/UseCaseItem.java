package com.ibm.webest.gui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import com.ibm.webest.gui.action.SizingAction;
import com.ibm.webest.gui.utils.ManagedBeanUtils;
import com.ibm.webest.persistence.model.UseCase;
import com.ibm.webest.persistence.model.UseCasePack;

/**
 * This class helps to create a tree for the UseCase-Table in GUI. You should
 * initialize it with a list of UseCasePacks. Then the whole tree will build by
 * itself, because a of UseCasePack contains all child-elements (UseCase) as a
 * list. The class also calculates the uucp and total for all use cases. The
 * logic for calculation is handled in processing and not in this class.
 * 
 * @author Florian Friedrichs
 */
public class UseCaseItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private int uucp = 0;
	private double total = 0.0;
	private Object object; // usecase or usecasepack or null (root)
	private List<UseCaseItem> children;

	/**
	 * Constructor for the root node (first level in tree)
	 * 
	 * @param useCasePacks
	 *            saves the list of use case packs as an use case item. This
	 *            list builds the tree recursive.
	 */
	public UseCaseItem(List<UseCasePack> useCasePacks) {
		this.object = null;
		List<UseCaseItem> useCaseList = new ArrayList<UseCaseItem>();
		for (UseCasePack ucp : useCasePacks)
			useCaseList.add(new UseCaseItem(ucp));
		this.children = useCaseList;
	}

	/**
	 * Constructor for a use case pack (second level in tree)
	 * 
	 * @param useCasePack
	 *            saves the use use case pack as an use case item
	 */
	public UseCaseItem(UseCasePack useCasePack) {
		this.object = useCasePack;
		List<UseCaseItem> useCaseList = new ArrayList<UseCaseItem>();
		for (UseCase uc : useCasePack.getUseCases())
			useCaseList.add(new UseCaseItem(uc));
		this.children = useCaseList;
	}

	/**
	 * Constructor for a new use case (third level in tree)
	 * 
	 * @param useCase
	 *            the use case to save it as an use case item
	 */
	public UseCaseItem(UseCase useCase) {
		this.object = useCase;
		this.children = null;
		this.reCalc();
	}

	/**
	 * (re-)calculate all totals und uucp for the tree recursive.
	 */
	public void reCalc() {

		// this is the root... do nothing and get into the tree
		if (this.object == null) {
			for (UseCaseItem uci : this.getChildren())
				uci.reCalc();

			// this is a use case pack... do nothing and get into the use cases
			// for every use case pack
		} else if (this.object instanceof UseCasePack) {
			for (UseCaseItem uci : this.getChildren())
				uci.reCalc();

			// now this is a use case... validate it and if it succeeds ->
			// calculate total and uucp
		} else if (this.object instanceof UseCase) {
			UseCase u = (UseCase) this.object;
			Set<ConstraintViolation<UseCase>> validate = Validation
					.buildDefaultValidatorFactory().getValidator().validate(u);
			if (validate.isEmpty()) {
				setUucp(calcUucp(u));
				setTotal((double) getUucp() * u.getMultiplier());
			}
		}

	}

	/**
	 * uses the calculation for uucp of service layer.
	 * 
	 * @param useCase
	 *            the given use case to calculate the values for
	 * @return the calculated uucp of server layer
	 */
	private int calcUucp(UseCase useCase) {
		// try to calculate the uucp
		return ManagedBeanUtils.getBean(SizingAction.class, "sizingAction")
				.calcUucp(useCase);
	}

	/**
	 * getter for total value
	 * 
	 * @return total value
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * setter for total value
	 * 
	 * @param total
	 */
	public void setTotal(double total) {
		this.total = Math.round(total * 100.) / 100.;
	}

	/**
	 * getter for uucp value
	 * 
	 * @return uucp value
	 */
	public int getUucp() {
		return uucp;
	}

	/**
	 * setter for uucp value
	 * 
	 * @param uucp
	 */
	public void setUucp(int uucp) {
		this.uucp = uucp;
	}

	/**
	 * getter for container-object (null, use case pack or use case)
	 * 
	 * @return container-object (null, use case pack or use case)
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * setter for container-object (null, use case pack or use case)
	 * 
	 * @param object
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * getter for the list of children-elements
	 * 
	 * @return the list of children-elements
	 */
	public List<UseCaseItem> getChildren() {
		return this.children;
	}

	/**
	 * setter for the list of children-elements
	 * 
	 * @param children
	 */
	public void setChildren(List<UseCaseItem> children) {
		this.children = children;
	}

}
