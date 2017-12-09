package co.kr.sky.hymnbible;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

/*
* tts 이동 소스
* if (Build.VERSION.SDK_INT >= 14){
                        Intent intent = new Intent();
                        intent.setAction("com.android.settings.TTS_SETTINGS");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent();
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.TextToSpeechSettings"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
* */
public class IntroActivity extends Activity {

	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_CONTACTS,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.SEND_SMS,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_PHONE_STATE,
			Manifest.permission.READ_CONTACTS
			
	};

	Handler mHandler = new Handler();
	Runnable r= new Runnable() {
		@Override
		public void run() {
			MainActivity.ffasdfasdf = 1;
			finish();


		}
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //추가한 라인
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();
		int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

		if(permissionCheck== PackageManager.PERMISSION_DENIED){
			// 권한 없음
			Log.e("SKY", "권한 없음");
			ActivityCompat.requestPermissions(this,
					PERMISSIONS_STORAGE,
					1);
		}else{
			// 권한 있음
			Log.e("SKY", "권한 있음");
			mHandler.postDelayed(r, 1000);
		}

	}

	public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
		switch (requestCode) {
		case 1: {
			// If request is cancelled, the result arrays are empty.
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Log.e("SKY" , "성공");
				Log.e("SKY" , "permissions SIZE :: " + permissions.length);
				if (permissions.length == 6) {
					mHandler.postDelayed(r, 1000);
				}else{
					AlertDialog.Builder alert = new AlertDialog.Builder(IntroActivity.this, AlertDialog.THEME_HOLO_LIGHT);
					alert.setTitle("알림");
					alert.setMessage("이앱은 모두 허용하여야만 사용이 가능한 어플리케이션 입니다.\n다시 이앱에 대한 권한을 설정하기 위해서 '설정/애플리케이션관리자/성경찬송/권한' 으로 이동하여 모두 허용으로 변경해주세요.");
					alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							mHandler.postDelayed(r, 1000);
						}
					});
					/*
					// Cancel 버튼 이벤트
					alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							finish();
						}
					});
					*/
					alert.show();
				}

			} else {
				Log.e("SKY" , "실패");
			}
			return;
		}
		}
	}
}