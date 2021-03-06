package com.sybase365.mobiliser.web.btpn.bank.pages.portal.manageproducts;

import com.sybase365.mobiliser.web.btpn.bank.beans.ManageProductsApproveBean;
import com.sybase365.mobiliser.web.btpn.bank.common.panels.ManageProductsApproveConfirmPanel;
import com.sybase365.mobiliser.web.btpn.bank.pages.portal.selfcare.BtpnBaseBankPortalSelfCarePage;

/**
 * This is the Manage Products page for bank portals.
 * 
 * @author Vikram Gunda
 */
public class ManageProductsApproveConfirmPage extends BtpnBaseBankPortalSelfCarePage {

	/**
	 * Constructor for this page.
	 */
	public ManageProductsApproveConfirmPage(final ManageProductsApproveBean approveBean) {
		super();
		initThisPageComponents(approveBean);
	}

	/**
	 * This method should be used to initialise all components of the page which have to be available each time a fresh
	 * instance of the page has to be created.
	 */
	protected void initThisPageComponents(final ManageProductsApproveBean approveBean) {
		add(new ManageProductsApproveConfirmPanel("manageProductsApproveConfirmPanel", this, approveBean));

	}
}
