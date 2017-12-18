package co.kr.sky.hymnbible;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.kr.sky.hymnbible.common.DEFINE;

public class ImageViewActivity extends Activity{

	public static WebView ImageWebView;

	private LinearLayout bottomview01;
	ImageView imgView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view);

		imgView = (ImageView)findViewById(R.id.imgView);
		bottomview01 = (LinearLayout)findViewById(R.id.bottomview01);
		Log.e("SKY" , "url go :: " + getIntent().getStringExtra("url"));


		if (getIntent().getStringExtra("shareurl").equals("안씀")){
			bottomview01.setVisibility(View.GONE);
		}
		//setting_web();
		findViewById(R.id.down).setOnClickListener(btnListener);
		findViewById(R.id.share).setOnClickListener(btnListener);
		findViewById(R.id.close).setOnClickListener(btnListener);
		new DownloadImageTask(imgView)
				.execute(getIntent().getStringExtra("url"));


	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}
	//버튼 리스너 구현 부분 
		View.OnClickListener btnListener = new View.OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {

				case R.id.close:	
					finish();
					break;
					case R.id.down:

						AlertDialog.Builder alert = new AlertDialog.Builder(ImageViewActivity.this, AlertDialog.THEME_HOLO_LIGHT);
						alert.setTitle("배경화면 다운로드");
						alert.setMessage("저장이 완료되면,\n갤러리에서 배경화면을\n적용할 수 있습니다.");
						alert.setPositiveButton("저장", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								String url = getIntent().getStringExtra("url");
								String filename[] = url.split("/");
								Date d = new Date();
								String s = d.toString();
								System.out.println("현재날짜 : "+ s);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
								System.out.println("현재날짜 : "+ sdf.format(d));
								String date = sdf.format(d);
								Uri source = Uri.parse(url);
								// Make a new request pointing to the .apk url
								DownloadManager.Request request = new DownloadManager.Request(source);
								// appears the same in Notification bar while downloading
								request.setDescription("Description for the DownloadManager Bar");
								request.setTitle(filename[filename.length-1]);
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
									request.allowScanningByMediaScanner();
									request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
								}
								// save the file in the "Downloads" folder of SDCARD
								request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "event_" + date  +  ".xls");
								// get download service and enqueue file
								DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
								manager.enqueue(request);
							}
						});

						// Cancel 버튼 이벤트
						alert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});

						alert.show();


						break;
					case R.id.share:
						//String message = "은혜로운 말씀카드가 도착했습니다. 말씀카드 보기 " + getIntent().getStringExtra("shareurl")+ " 무료 성경과 찬송반주 어플 성경과찬송뉴";
						String message = "성경과찬송뉴로부터\n은혜로운 말씀카드와 QT가\n도착했습니다.\n\n말씀카드보기\n"+getIntent().getStringExtra("shareurl")+"\n\n대한미국 기독교 대표앱\n찬양전곡 리얼반주MR 과\n4성부 파트, 노래 악보등\n찬양의 모든것제공\n아이폰및 안드로이드\n앱스토어에서 '성경과찬송뉴'\n검색 설치해보세요";
						Intent msg = new Intent(Intent.ACTION_SEND);
						msg.addCategory(Intent.CATEGORY_DEFAULT);
						msg.putExtra(Intent.EXTRA_SUBJECT, "성경과찬송-뉴");
						msg.putExtra(Intent.EXTRA_TEXT, message);
						msg.setType("text/plain");
						startActivity(Intent.createChooser(msg, "공유"));
						break;

				}
			}
		};
	//버튼 리스너 구현 부분 
	private void setting_web(){
		ImageWebView = (WebView)findViewById(R.id.web);
		//홍진:openWindow 사용
		//setWebViewOpenWindow();
		ImageWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//팝업(window.open) 권한
		ImageWebView.getSettings().setSupportMultipleWindows(true); //팝업을허용하고 setSupportMultipleWindows를 주지않으면 url이로딩 된다
		ImageWebView.getSettings().setJavaScriptEnabled(true); 
		ImageWebView.getSettings().setDomStorageEnabled(true);
		ImageWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ImageWebView.getSettings().setBuiltInZoomControls(true);
		ImageWebView.getSettings().setSupportZoom(true);
		ImageWebView.setInitialScale(1);
		ImageWebView.getSettings().setLoadWithOverviewMode(true) ;
		ImageWebView.getSettings().setUseWideViewPort(true);
		ImageWebView.setBackgroundColor(Color.WHITE);

		if(android.os.Build.VERSION.SDK_INT >= 11)
		{
			getWindow().addFlags(16777216);
		}

		ImageWebView.loadUrl(getIntent().getStringExtra("url"));
	}
}
