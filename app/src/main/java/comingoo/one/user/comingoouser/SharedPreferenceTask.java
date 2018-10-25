package comingoo.one.user.comingoouser.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceTask {
    private final SharedPreferences mPrefs;

    public SharedPreferenceTask(Context context) {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String PREF_Key= "User_Ride_Req_Cancel";

    public int getCancelNumber() {
        int str = mPrefs.getInt(PREF_Key, 0);
        return str;
    }

    public void setCancelNumber(int number) {
        SharedPreferences.Editor mEditor = mPrefs.edit();
        mEditor.putInt(PREF_Key, number);
        mEditor.commit();
    }
}
