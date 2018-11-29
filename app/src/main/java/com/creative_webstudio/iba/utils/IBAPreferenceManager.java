package com.creative_webstudio.iba.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.creative_webstudio.iba.BuildConfig;

import java.util.Calendar;
public class IBAPreferenceManager {

    static final String PREFS_NAME = "IBAPreference";
    Context mContext;
    SharedPreferences prefs;
    Editor prefsEditor;

    public IBAPreferenceManager(Context mContext) {
        this.mContext = mContext;
        prefs = mContext.getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        prefsEditor = prefs.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return prefs;
    }

    public Editor getPrefsEditor() {
        return prefsEditor;
    }

    public String fromPreference(String key,
                                 String defaultValue) {
        try {
            return prefs.getString(key, defaultValue);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("TelenorPrefs", "Error -> " + e.getMessage());
            }
            return defaultValue;
        }
    }

    public void toPreference(String key, String value) {
        try {
            prefsEditor.putString(key, value);
            prefsEditor.commit();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("TelenorPrefs", "Error -> " + e.getMessage());
            }
        }
    }

    public void toPreference(String key, boolean value) {
        try {
            prefsEditor.putBoolean(key, value);
            prefsEditor.commit();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("TelenorPrefs", "Error -> " + e.getMessage());
            }
        }
    }

    public void removePreference(String key) {
        String encryptedKey = "";
        try {
            encryptedKey = key;
            prefsEditor.remove(encryptedKey);
            prefsEditor.commit();
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("TelenorPrefs", "Error -> " + e.getMessage());
            }
        }
    }

    public void clear() {
        prefsEditor.clear().commit();
    }


//    public Language getLanguage() {
//       /* SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
//        return Language.get(prefs.getString("pref_key_app_lang", "en_US"));*/
//        String lang = fromPreference("LANG", "");
//        if (lang.equals("en_US")) {
//            return Language.ENG;
//        } else {
//            return Language.MM;
//        }
//    }

    public boolean isConfigured() {
        return prefs.getString("ConfigVersionID", null) != null && prefs.getString("MyBillFun", null) != null;
    }

    public String getSubscriberNum() {
        return prefs.getString("SubNum", null);
    }

    public String getSubscriberNum(boolean startWithZero) {
        String phNum = prefs.getString("SubNum", null);
        if (!TextUtils.isEmpty(phNum) && !phNum.startsWith("0")) {
            phNum = "0" + phNum;
        }
        return phNum;
    }

    public String getSessionId() {
        return fromPreference("SessionId", "");
    }

    public String getPlanEng() {
        return fromPreference("GetPlanEng", "");
    }

    public String getPlanUni() {
        return fromPreference("GetPlanUni", "");
    }

    public String getExpiryDate() {
        return fromPreference("ExpiryDate", "");
    }

    public boolean accessTokenHasExpired() {
        String expirationDate = fromPreference("ExpirationDate", "");
        if (!TextUtils.isEmpty(expirationDate)) {
            try {
                return Calendar.getInstance().getTimeInMillis() > Long.parseLong(expirationDate);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e("CheckTokenExpiration", e.getMessage());
                }
                return true;
            }
        }
        return true;
    }
}
