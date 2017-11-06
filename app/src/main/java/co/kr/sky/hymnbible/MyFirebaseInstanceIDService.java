package co.kr.sky.hymnbible;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import co.kr.sky.hymnbible.common.Check_Preferences;
import co.kr.sky.hymnbible.fun.CommonUtil;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    CommonUtil dataSet = CommonUtil.getInstance();

    private static final String TAG = "SKY";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + token);
        dataSet.REG_ID = token;
        Check_Preferences.setAppPreferences(getApplicationContext() , "REG_ID" , token);
        //dataSet.REG_ID = token;
    }

}
