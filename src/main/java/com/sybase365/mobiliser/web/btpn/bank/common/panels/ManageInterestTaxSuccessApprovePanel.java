package com.sybase365.mobiliser.web.btpn.bank.common.panels;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sybase365.mobiliser.web.btpn.bank.beans.ManageInterestTaxApproveBean;
import com.sybase365.mobiliser.web.btpn.bank.pages.portal.manageinteresttax.ManageInterestTaxApprovePage;
import com.sybase365.mobiliser.web.btpn.bank.pages.portal.selfcare.BtpnBaseBankPortalSelfCarePage;
import com.sybase365.mobiliser.web.btpn.common.components.AmountLabel;

/**
 * This is the Manage Interest Success Approve Panel for bank portals. This panel consists of adding fee as fixed, slab and sharing.
 * 
 * @author Feny Yanti
 */
public class ManageInterestTaxSuccessApprovePanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ManageInterestTaxSuccessApprovePanel.class);
	
	private BtpnBaseBankPortalSelfCarePage basePage;
	private ManageInterestTaxApproveBean interestTaxBean;
	private String flag;

	public ManageInterestTaxSuccessApprovePanel(final String id, final BtpnBaseBankPortalSelfCarePage basePage,
			ManageInterestTaxApproveBean interestTaxBean, String flag) {
		super(id);
		this.basePage = basePage;
		this.interestTaxBean = interestTaxBean;
		this.flag = flag;
		constructPanel();
	}

	
	private void constructPanel() {
		
		log.info(" ### (ManageInterestTaxApproveSuccessPanel) constructPanel ###");
		
		final Form<ManageInterestTaxSuccessApprovePanel> form = new Form<ManageInterestTaxSuccessApprovePanel>("interestTaxSucAprForm",
			new CompoundPropertyModel<ManageInterestTaxSuccessApprovePanel>(this));
		
		// Add feedback panel for Error Messages
		final FeedbackPanel feedBackPanel = new FeedbackPanel("errorMessages");
		form.add(feedBackPanel);
		
		/// Add interest management fields
		form.add(new Label("interestTaxBean.customerIdentifier"));
		form.add(new Label("interestTaxBean.customerIdentifierType"));
		form.add(new Label("customerTypeId", interestTaxBean.getCustomerTypeId()==null ? "-" : interestTaxBean.getCustomerTypeId().getIdAndValue()));
		form.add(new Label("interestTaxBean.paymentInstrumentId"));
		form.add(new Label("paymentInstrumentTypeId", interestTaxBean.getPaymentInstrumentTypeId()==null? "-" : interestTaxBean.getPaymentInstrumentTypeId().getIdAndValue()));
		form.add(new Label("accrueGLCode", interestTaxBean.getAccrueGLCode().getIdAndValue()));
		form.add(new Label("taxGLCode", interestTaxBean.getTaxGLCode().getIdAndValue()));
		form.add(new Label("interestTaxBean.validFrom"));
		form.add(new Label("interestTaxBean.fixedFee"));
		form.add(new Label("interestTaxBean.percentageFee"));
		form.add(new Label("interestTaxBean.maximumFee"));
		form.add(new Label("interestTaxBean.minimumFee"));
		form.add(new AmountLabel("interestTaxBean.thresholdAmount"));
		form.add(new Label("interestTaxBean.description"));
		form.add(new Label("interestTaxBean.note"));

		form.add(new AjaxButton("finishButton") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				setResponsePage(ManageInterestTaxApprovePage.class);
			};
		});
		
		// Add add Button
		add(form);
	}

}
