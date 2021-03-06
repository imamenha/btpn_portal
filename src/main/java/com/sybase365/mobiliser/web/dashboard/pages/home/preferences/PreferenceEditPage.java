package com.sybase365.mobiliser.web.dashboard.pages.home.preferences;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.sybase365.mobiliser.web.beans.PreferenceBean;
import com.sybase365.mobiliser.web.dashboard.pages.home.HomeMenuGroup;
import com.sybase365.mobiliser.web.dashboard.pages.home.preferences.model.IPreferencesTreeEntry;
import com.sybase365.mobiliser.web.dashboard.pages.home.preferences.model.IPreferencesNodeViewer;
import com.sybase365.mobiliser.web.util.Constants;

@AuthorizeInstantiation(Constants.PRIV_DASHBOARD_PREFS)
public class PreferenceEditPage extends HomeMenuGroup {

    private static final long serialVersionUID = 1L;
    private static final org.slf4j.Logger LOG = 
            org.slf4j.LoggerFactory.getLogger(PreferenceEditPage.class);

    private IPreferencesNodeViewer backPage;

    private IPreferencesTreeEntry selectedPreferencesNode;
    private PreferenceBean selectedPreferenceBean;

    public PreferenceEditPage(IPreferencesTreeEntry selectedNode, PreferenceBean prefBean, IPreferencesNodeViewer backPage) {
        super();
        this.selectedPreferencesNode = selectedNode;
        this.selectedPreferenceBean = prefBean;
        this.backPage = backPage;
        initPageComponents();
    }

    @SuppressWarnings("unchecked")
    protected void initPageComponents() {

        add(new PreferencePanel("preferencePanel", 
                this.selectedPreferencesNode,
                this.selectedPreferenceBean, backPage, this));

    }

    @Override
    protected Class getActiveMenu() {
	return PreferencesPage.class;
    }

}
