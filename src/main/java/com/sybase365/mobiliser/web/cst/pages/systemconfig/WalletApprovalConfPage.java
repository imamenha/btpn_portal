package com.sybase365.mobiliser.web.cst.pages.systemconfig;

import com.sybase365.mobiliser.web.common.panels.ApprovalConfigurationPanel;
import com.sybase365.mobiliser.web.util.Constants;

public class WalletApprovalConfPage extends BaseSystemConfigurationPage {
    @Override
    protected void initOwnPageComponents() {
	super.initOwnPageComponents();
	add(new ApprovalConfigurationPanel("approvalCustomerPanel", this, null,
		Constants.RESOURCE_BUNDLE_PI_TYPES));
    }

}
