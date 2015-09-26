package com.ibm.webest.gui.utils;

import java.util.Date;

import com.ibm.webest.persistence.model.Estimate;
import com.ibm.webest.persistence.model.Solution;
import com.ibm.webest.persistence.model.User;

/**
 * Utility class for text operations.
 * @author Gregor Schumm
 *
 */
public final class TextUtils {
	
	/**
	 * Adds a signature in the form <code>\n-----\n02/03/2011 - Last Name, First Name:\n</code> to the comment of the given estimate.
	 * @param e the estimate to add the comment signature to
	 * @param u the user to be displayed in the signature
	 */
	public static void addCommentSignature(Estimate e, User u) {
		e.setComment(getCommentSignature(e.getComment(), u));
	}

	/**
	 * Adds a signature in the form <code>\n-----\n02/03/2011 - Last Name, First Name:\n</code> to the comment of the given solution.
	 * @param s the solution to add the comment signature to
	 * @param u the user to be displayed in the signature
	 */
	public static void addCommentSignature(Solution s,
			User u) {
		s.setComment(getCommentSignature(s.getComment(), u));
	}
	
	private static String getCommentSignature(String old, User u) {
		return String.format("%s\n-----\n%tD - %s, %s:\n",old == null ? "" : old, new Date(),
				u.getLastName(), u.getFirstName());
	}
}
