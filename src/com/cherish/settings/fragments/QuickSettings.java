package com.cherish.settings.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import com.cherish.settings.preferences.CustomSeekBarPreference;
import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.cherish.settings.preferences.SystemSettingSwitchPreference;
import com.cherish.settings.preferences.SystemSettingListPreference;
import com.cherish.settings.preferences.SystemSettingEditTextPreference;
import com.cherish.settings.preferences.SystemSettingMasterSwitchPreference;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import android.provider.SearchIndexableResource;
import java.util.List;
import java.util.ArrayList;

@SearchIndexable(forTarget = SearchIndexable.ALL & ~SearchIndexable.ARC)
public class QuickSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

   private static final String FOOTER_TEXT_STRING = "footer_text_string";
   private static final String STATUS_BAR_CUSTOM_HEADER = "status_bar_custom_header";

    private SystemSettingEditTextPreference mFooterString;
    private SystemSettingMasterSwitchPreference mCustomHeader;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.cherish_settings_quicksettings);

        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mCustomHeader = (SystemSettingMasterSwitchPreference) findPreference(STATUS_BAR_CUSTOM_HEADER);
        int qsHeader = Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_CUSTOM_HEADER, 0);
        mCustomHeader.setChecked(qsHeader != 0);
        mCustomHeader.setOnPreferenceChangeListener(this);

        mFooterString = (SystemSettingEditTextPreference) findPreference(FOOTER_TEXT_STRING);
        mFooterString.setOnPreferenceChangeListener(this);
        String footerString = Settings.System.getString(getContentResolver(),
                FOOTER_TEXT_STRING);
        if (footerString != null && footerString != "")
            mFooterString.setText(footerString);
        else {
            mFooterString.setText("#KeepTheLove");
            Settings.System.putString(getActivity().getContentResolver(),
                    Settings.System.FOOTER_TEXT_STRING, "#KeepTheLove");
        }
	}

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
         if (preference == mFooterString) {
            String value = (String) newValue;
            if (value != "" && value != null)
                Settings.System.putString(getActivity().getContentResolver(),
                        Settings.System.FOOTER_TEXT_STRING, value);
            else {
                mFooterString.setText("#KeepTheLove");
                Settings.System.putString(getActivity().getContentResolver(),
                        Settings.System.FOOTER_TEXT_STRING, "#KeepTheLove");
            }
            return true;
            } else if (preference == mCustomHeader) {
            boolean header = (Boolean) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.STATUS_BAR_CUSTOM_HEADER, header ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CHERISH_SETTINGS;
    }
	
	/**
     * For Search.
     */

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {

                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.cherish_settings_quicksettings;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };

}
