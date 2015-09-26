package com.ibm.webest.gui.utils;

import org.apache.myfaces.extensions.validator.core.ExtValContext;
import org.apache.myfaces.extensions.validator.core.renderkit.ExtValRendererProxy;
import org.apache.myfaces.extensions.validator.core.startup.AbstractStartupListener;

/**
 * Workaround for a bug in Trinidad tables in conjunction with ext-val.
 * @author Gregor Schumm
 *
 */
public class MyFacesStartupListener extends AbstractStartupListener {
	private static final long serialVersionUID = 5040478388486609422L;

	@Override
	protected void init() {
		ExtValContext.getContext().addGlobalProperty(ExtValRendererProxy.KEY,
				null, true);
	}
}
