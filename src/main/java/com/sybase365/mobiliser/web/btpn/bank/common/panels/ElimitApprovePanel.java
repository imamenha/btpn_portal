package com.sybase365.mobiliser.web.btpn.bank.common.panels;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.btpnwow.core.limitex.services.contract.v1_0.FindPendingApprovalLimitExRequest;
import com.btpnwow.core.limitex.services.contract.v1_0.FindPendingApprovalLimitExResponse;
import com.btpnwow.core.limitex.services.contract.v1_0.PendingApprovalLimitEx;
import com.sybase365.mobiliser.util.tools.wicketutils.ErrorIndicator;
import com.sybase365.mobiliser.web.btpn.application.pages.BtpnMobiliserBasePage;
import com.sybase365.mobiliser.web.btpn.bank.beans.ElimitBean;
import com.sybase365.mobiliser.web.btpn.bank.common.dataproviders.LimitExApproveDataProvider;
import com.sybase365.mobiliser.web.btpn.bank.pages.portal.selfcare.BtpnBaseBankPortalSelfCarePage;
import com.sybase365.mobiliser.web.btpn.bank.pages.portal.limit.ElimitApproveDetailsPage;
import com.sybase365.mobiliser.web.btpn.common.behaviours.DateHeaderContributor;
import com.sybase365.mobiliser.web.btpn.common.components.BtpnCustomPagingNavigator;
import com.sybase365.mobiliser.web.btpn.util.BtpnConstants;
import com.sybase365.mobiliser.web.btpn.util.BtpnOrderByOrder;
import com.sybase365.mobiliser.web.btpn.util.ConverterUtils;
import com.sybase365.mobiliser.web.util.PortalUtils;

/**
 * This is the CashOutPanel page for bank portals.
 * 
 * @author Narasa Reddy
 */
public class ElimitApprovePanel extends Panel {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(ElimitApprovePanel.class);

	private FeedbackPanel feedBack;

	protected BtpnMobiliserBasePage basePage;
	
	protected BtpnMobiliserBasePage mobBasePage;
	
	private WebMarkupContainer limitContainer;
	
	private LimitExApproveDataProvider limitExDataProvider;

	private static final String WICKET_ID_PAGEABLE = "pageable";

	private static final String WICKET_ID_cashOutTotalItems = "limitHeader";

	private static final String WICKET_ID_cashOutNavigator = "limitNavigator";

	private String limitTotalItemString;

	private int limitStartIndex = 0;

	private int limitEndIndex = 0;
	
	private Component decriptionComponent;
	
	private Component statusComponent;
	
	private Component dateFrom;
	
	private Component dateTo;

	private ElimitBean limitBean;
	
	
	
	
	

	public ElimitApprovePanel(String id, BtpnBaseBankPortalSelfCarePage basePage) {
		super(id);
		this.basePage = basePage;
		this.mobBasePage = basePage;
		// Adds the panel
		addDateHeaderContributor();
		constructPanel();
		
	}
	

	protected void constructPanel() {

		final Form<ElimitApprovePanel> form = new Form<ElimitApprovePanel>("limitApproveForm",
			new CompoundPropertyModel<ElimitApprovePanel>(this));

		// Add feedback panel for Error Messages
		feedBack = new FeedbackPanel("errorMessages");
		feedBack.setOutputMarkupId(true);
		feedBack.setOutputMarkupPlaceholderTag(true);
		form.add(feedBack);
		
		

		form.add(decriptionComponent = new TextField<String>(
				"limitBean.description").add(
				BtpnConstants.REGISTRATION_DISPLAY_NAME_MAX_LENGTH).add(
				new ErrorIndicator()));
		decriptionComponent.setOutputMarkupId(true);

		form.add(statusComponent = new TextField<String>("limitBean.status")
				.add(BtpnConstants.REGISTRATION_DISPLAY_NAME_MAX_LENGTH).add(
						new ErrorIndicator()));
		statusComponent.setOutputMarkupId(true);

		DateTextField fromDate = (DateTextField) DateTextField
				.forDatePattern("limitBean.dateFrom",
						BtpnConstants.ID_EXPIRY_DATE_PATTERN)
				//.add(DateValidator.minimum(new Date(),BtpnConstants.ID_EXPIRY_DATE_PATTERN))
				.setRequired(false).add(new ErrorIndicator());
		dateFrom = fromDate;
		form.add(dateFrom);

		DateTextField toDate = (DateTextField) DateTextField
				.forDatePattern("limitBean.dateTo",
						BtpnConstants.ID_EXPIRY_DATE_PATTERN)
				//.add(DateValidator.minimum(new Date(), BtpnConstants.ID_EXPIRY_DATE_PATTERN))
				.setRequired(false).add(new ErrorIndicator());
		
		dateTo = toDate;
		form.add(dateTo);

		limitContainer = new WebMarkupContainer("limitContainer");
		cashOutListDataView(limitContainer);
		limitContainer.setOutputMarkupId(true);
		limitContainer.setOutputMarkupPlaceholderTag(true);
		limitContainer.setVisible(false);
		form.add(limitContainer);

		form.add(new AjaxButton("submitButton") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (!PortalUtils.exists(limitBean)) {
					limitBean = new ElimitBean();
				}
				limitBean.setLimitList(new ArrayList<ElimitBean>());
				handleSearchLimit(target);
			};

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(feedBack);
				target.addComponent(limitContainer);
				super.onError(target, form);
			}
		});

		add(form);
	}

	/**
	 * This method populate the limit details.
	 */
	protected void cashOutListDataView(
			final WebMarkupContainer dataViewContainer) {
		limitExDataProvider = new LimitExApproveDataProvider("customerId");

		final DataView<ElimitBean> dataView = new DataView<ElimitBean>(
				WICKET_ID_PAGEABLE, limitExDataProvider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				final LimitExApproveDataProvider dataProvider = (LimitExApproveDataProvider) internalGetDataProvider();
				dataProvider.setLimitList(limitBean.getLimitList());
				refreshTotalItemCount();
				super.onBeforeRender();
			}

			@Override
			protected void populateItem(final Item<ElimitBean> item) {
				final ElimitBean entry = item.getModelObject();

				// Add the customer id Link
				final AjaxLink<ElimitBean> customerIdLink = new AjaxLink<ElimitBean>(
						"idLink", item.getModel()) {
					private static final long serialVersionUID = 1L;
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						
						final ElimitBean limitBean = item.getModelObject();
						setResponsePage(new ElimitApproveDetailsPage(limitBean));
					}
				};
				
				customerIdLink.add(new Label("id", entry.getTaskId()));
				item.add(customerIdLink);

				item.add(new Label("description", entry.getDescription()));

				item.add(new Label("status", entry.getStatus() ));

				item.add(new Label("creationDate", String.valueOf(entry
						.getCreationDate())));

				item.add(new Label("creation", String.valueOf(entry
						.getCreator())));

				final String cssStyle = item.getIndex() % 2 == 0 ? BtpnConstants.DATA_VIEW_EVEN_ROW_CSS
						: BtpnConstants.DATA_VIEW_ODD_ROW_CSS;
				item.add(new SimpleAttributeModifier("class", cssStyle));
			}

			private void refreshTotalItemCount() {
				final int size = internalGetDataProvider().size();
				limitTotalItemString = new Integer(size).toString();
				if (size > 0) {
					limitStartIndex = getCurrentPage() * getItemsPerPage() + 1;
					limitEndIndex = limitStartIndex + getItemsPerPage() - 1;
					if (limitEndIndex > size) {
						limitEndIndex = size;
					}
				} else {
					limitStartIndex = 0;
					limitEndIndex = 0;
				}
			}

			@Override
			public boolean isVisible() {
				final LimitExApproveDataProvider dataProvider = (LimitExApproveDataProvider) internalGetDataProvider();
				dataProvider.setLimitList(limitBean.getLimitList());
				return limitBean.getLimitList().size() != 0;

			}
		};

		// Add the navigation
		final BtpnCustomPagingNavigator navigator = new BtpnCustomPagingNavigator(
				WICKET_ID_cashOutNavigator, dataView) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return limitBean != null
						&& limitBean.getLimitList().size() == 0;
			}
		};
		navigator.setOutputMarkupId(true);
		navigator.setOutputMarkupPlaceholderTag(true);
		dataViewContainer.add(navigator);
		dataView.setItemsPerPage(20);

		// Add the header
		IModel<String> headerDisplayModel = new AbstractReadOnlyModel<String>() {

			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				final String displayTotalItemsText = ElimitApprovePanel.this
						.getLocalizer().getString("limit.totalitems.header",
								ElimitApprovePanel.this);
				return String.format(displayTotalItemsText,
						limitTotalItemString, limitEndIndex, limitEndIndex);
			}

		};

		final Label cashOutHeader = new Label(WICKET_ID_cashOutTotalItems,
				headerDisplayModel) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return limitBean != null
						&& limitBean.getLimitList().size() == 0;
			}
		};
		cashOutHeader.setOutputMarkupId(true);
		cashOutHeader.setOutputMarkupPlaceholderTag(true);
		dataViewContainer.add(cashOutHeader);

		// Add the no items label.
		dataViewContainer.add(new Label("no.items", getLocalizer().getString(
				"cashOut.emptyRecordsMessage", this)) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return limitBean != null
						&& limitBean.getLimitList().size() == 0;

			}
		}.setRenderBodyOnly(true).setOutputMarkupPlaceholderTag(true));

		// Add the sort providers
		dataViewContainer.add(new BtpnOrderByOrder("orderByCustomerId",
				"customerId", limitExDataProvider, dataView));
		dataViewContainer.add(new BtpnOrderByOrder("orderByMobileNumber",
				"mobileNumber", limitExDataProvider, dataView));
		dataViewContainer.add(new BtpnOrderByOrder("orderByDisplayName",
				"displayName", limitExDataProvider, dataView));
		dataViewContainer.addOrReplace(dataView);

	}

	/**
	 * This method fetches the list of CashOut Transaction List.
	 */
	private void handleSearchLimit(AjaxRequestTarget target) {
		getLimitExTransactionList();
		limitContainer.setVisible(true);
		target.addComponent(limitContainer);
		target.addComponent(feedBack);

	}
	
	
	private void getLimitExTransactionList() {
		
		List<ElimitBean> limitList = new ArrayList<ElimitBean>();
		final Component component = null;
		
		try {
			final FindPendingApprovalLimitExRequest request = mobBasePage.getNewMobiliserRequest(FindPendingApprovalLimitExRequest.class);
			
			GregorianCalendar calFrom = new GregorianCalendar();
			GregorianCalendar calTo = new GregorianCalendar();
			XMLGregorianCalendar xmlFrom = null;
			XMLGregorianCalendar xmlTo = null;
			
			if(limitBean.getDateFrom()!=null){
				calFrom.setTime(limitBean.getDateFrom());
				xmlFrom = DatatypeFactory.newInstance().newXMLGregorianCalendar(calFrom);
			}
			
			if(limitBean.getDateTo()!=null){
				calTo.setTime(limitBean.getDateTo());
				xmlTo = DatatypeFactory.newInstance().newXMLGregorianCalendar(calTo);
			}

			request.setCheckerId(1L);
			request.setCreationDateFrom(xmlFrom);
			request.setCreationDateTo(xmlTo);
			request.setDescription(limitBean.getDescription());
			final FindPendingApprovalLimitExResponse response = mobBasePage.limitExClient.findPendingApprovalLimitEx(request);
			
			if (mobBasePage.evaluateBankPortalMobiliserResponse(response)) {
				
				for(PendingApprovalLimitEx pend : response.getLimit()){
					ElimitBean bean = ConverterUtils.convertToLimitBean(pend.getLimit(), component, mobBasePage);
					bean.setTaskId(pend.getTaskId());
					bean.setStatus(pend.getLimit().getStatus() + " (" + pend.getStatus() + ")");
					bean.setCreator(pend.getLimit().getCreator());
					limitList.add(bean);
				}
			} else {
				error(getLocalizer().getString("error.search", this));
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(
					"#An error occurred while calling getLimit service.",
					ex);
			error(getLocalizer().getString("error.exception", this));
		}

		limitBean.setLimitList(limitList);

	}

	
	/**
	 * Adds the date header contributor
	 */
	protected void addDateHeaderContributor() {
		final String chooseDtTxt = this.getLocalizer().getString("datepicker.chooseDate", basePage);
		final String locale = this.getLocale().getLanguage();
		add(new HeaderContributor(new DateHeaderContributor(locale, BtpnConstants.DATE_FORMAT_PATTERN_PICKER,
			chooseDtTxt)));
	}
	
	
	

}
