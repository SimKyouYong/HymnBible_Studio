package co.kr.sky.hymnbible;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.kr.sky.hymnbible.common.DEFINE;
import co.kr.sky.hymnbible.common.RealPathUtil;


public class ChurchSearchWebvie extends Activity {
    private static final String TYPE_IMAGE = "file/*";

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri> filePathCallbackNormal;
    private ValueCallback<Uri[]> filePathCallbackLollipop;
    private final static int FILECHOOSER_NORMAL_REQ_CODE = 1;
    private final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2;
    private final static int PICK_IMAGE_REQ_CODE = 3;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private static final int INPUT_FILE_REQUEST_CODE = 1;

    private WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_churchweb);

        web = (WebView)findViewById(R.id.web);
        setting_web();

    }
    private void setting_web(){
        web.setWebViewClient(new ITGOWebChromeClient());
        web.setWebChromeClient(new SMOWebChromeClient(this));
        //홍진:openWindow 사용
        //setWebViewOpenWindow();
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//팝업(window.open) 권한
        web.getSettings().setSupportMultipleWindows(true); //팝업을허용하고 setSupportMultipleWindows를 주지않으면 url이로딩 된다
        web.getSettings().setJavaScriptEnabled(true);
//		BibleWeb.addJavascriptInterface(new AndroidBridge(), "android");
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setSupportZoom(true);
        web.loadUrl(DEFINE.SEARCH_CRUCH);
        if(Build.VERSION.SDK_INT >= 11)
        {
            getWindow().addFlags(16777216);
        }

    }
    /*****************
     * @Class WebChromeClient
     *****************/
    class SMOWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private Activity mActivity;

        public SMOWebChromeClient(Activity activity) {
            this.mActivity = activity;
        }

        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            //Toast.makeText(getApplicationContext(), "message :  " + message, 0).show();
            Log.e("SKY" , "MESSAGE :: " + message);

            if(message.equals("등록되었습니다.")){
                new AlertDialog.Builder(ChurchSearchWebvie.this ,AlertDialog.THEME_HOLO_LIGHT).setTitle("확인").setMessage(message).setPositiveButton(
                        android.R.string.ok, new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                result.confirm();
                                finish();
                            }
                        }).setCancelable(false).create().show();
                return true;
            }
            new AlertDialog.Builder(ChurchSearchWebvie.this ,AlertDialog.THEME_HOLO_LIGHT).setTitle("확인").setMessage(message).setPositiveButton(
                    android.R.string.ok, new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            result.confirm();
                        }
                    }).setCancelable(false).create().show();

            return true;

        };
        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            // TODO Auto-generated method stub
            //return super.onJsConfirm(view, url, message, result);
            new AlertDialog.Builder(view.getContext())
                    .setTitle("알림")
                    .setMessage(message)
                    .setPositiveButton("네",
                            new AlertDialog.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                }
                            })
                    .setNegativeButton("아니오",
                            new AlertDialog.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    result.cancel();
                                }
                            })
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }

        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");


            startActivityForResult(Intent.createChooser(i, "파일 선택"), FILECHOOSER_RESULTCODE);
        }
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");


            startActivityForResult(Intent.createChooser(i, "파일 선택"), FILECHOOSER_RESULTCODE);
        }
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");

            startActivityForResult(Intent.createChooser(i, "파일 선택"), FILECHOOSER_RESULTCODE);
        }

        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            Log.e("SKY", "5.0+");
            System.out.println("WebViewActivity A>5, OS Version : " + Build.VERSION.SDK_INT + "\t onSFC(WV,VCUB,FCP), n=3");
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;
            imageChooser();
            return true;

        }
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            //super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
        }
        @Override
        public void onExceededDatabaseQuota(String url, String
                databaseIdentifier, long currentQuota, long estimatedSize,
                                            long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {

            super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
                    estimatedSize, totalUsedQuota, quotaUpdater);
        }
    }
    /*****************
     * @Class WebViewClient
     *****************/
    class ITGOWebChromeClient extends WebViewClient {


        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ChurchSearchWebvie.this);
            builder.setMessage("유효하지 않은 사이트 입니다.\n계속 진행하시겠습니까?");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
        @Override
        public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e("SKY", "errorCode = = = = = = = "+errorCode);

            switch(errorCode) {
                case ERROR_AUTHENTICATION: break;               // 서버에서 사용자 인증 실패
                case ERROR_BAD_URL: break;                           // 잘못된 URL
                case ERROR_CONNECT: break;                          // 서버로 연결 실패
                case ERROR_FAILED_SSL_HANDSHAKE: break;    // SSL handshake 수행 실패
                case ERROR_FILE: break;                                  // 일반 파일 오류
                case ERROR_FILE_NOT_FOUND: break;               // 파일을 찾을 수 없습니다
                case ERROR_HOST_LOOKUP: break;           // 서버 또는 프록시 호스트 이름 조회 실패
                case ERROR_IO: break;                              // 서버에서 읽거나 서버로 쓰기 실패
                case ERROR_PROXY_AUTHENTICATION: break;   // 프록시에서 사용자 인증 실패
                case ERROR_REDIRECT_LOOP: break;               // 너무 많은 리디렉션
                case ERROR_TIMEOUT: break;                          // 연결 시간 초과
                case ERROR_TOO_MANY_REQUESTS: break;     // 페이지 로드중 너무 많은 요청 발생
                case ERROR_UNKNOWN: break;                        // 일반 오류
                case ERROR_UNSUPPORTED_AUTH_SCHEME: break; // 지원되지 않는 인증 체계
                case ERROR_UNSUPPORTED_SCHEME: break;          // URI가 지원되지 않는 방식
            }
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("SKY", "shouldOverrideUrlLoading = = = = = = = "+url);


            if (url.matches(".*success*")) {
                //메인 페이지이기에 종료하기 띄운다!.
                Log.e("SKY", "success");
                finish();
                return true;
            }
            view.loadUrl(url);

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("SKY", "onPageStarted = = = = = = = "+url);


        }
        @Override
        public void onPageFinished(WebView view, String url){
            //			CookieSyncManager.getInstance().sync();
            super.onPageFinished(view, url);
            Log.e("SKY", "onPageFinished = = = = = = = "+url);

        }
    }
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("SKY" , "RESULT :: " + requestCode);
        Log.e("SKY" , "resultCode :: " + resultCode);
        Log.e("SKY" , "data :: " + data);
        if (data == null) {
            Log.e("SKY" , "data null:: ");
            if (mFilePathCallback != null) {
                Uri[] results = new Uri[]{Uri.parse("")};
                mFilePathCallback.onReceiveValue(results);
                mFilePathCallback = null;
            }
            return;
        }
        switch (requestCode) {
            case 1:
                if (requestCode == INPUT_FILE_REQUEST_CODE) {
                    Log.e("SKY" , "INPUT_FILE_REQUEST_CODE");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mFilePathCallback == null) {
                            super.onActivityResult(requestCode, resultCode, data);
                            return;
                        }
                        Uri[] results = new Uri[]{getResultUri(data)};

                        mFilePathCallback.onReceiveValue(results);
                        mFilePathCallback = null;
                    } else {
                        if (mUploadMessage == null) {
                            super.onActivityResult(requestCode, resultCode, data);
                            return;
                        }
                        Uri result = getResultUri(data);

                        Log.d(getClass().getName(), "openFileChooser : "+result);
                        mUploadMessage.onReceiveValue(result);
                        mUploadMessage = null;
                    }
                }
                break;



        }
    }
    private void imageChooser() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(getClass().getName(), "Unable to create Image File", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:"+photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType(TYPE_IMAGE);

        Intent[] intentArray;
        if(takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        chooserIntent.setType("image/*");
        chooserIntent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return imageFile;
    }
    private Uri getResultUri(Intent data) {
        Uri result = null;
        if(data == null || TextUtils.isEmpty(data.getDataString())) {
            // If there is not data, then we may have taken a photo
            if(mCameraPhotoPath != null) {
                result = Uri.parse(mCameraPhotoPath);
            }
        } else {
            String filePath = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                filePath = data.getDataString();
            } else {
                filePath = "file:" + RealPathUtil.getRealPath(this, data.getData());
            }
            result = Uri.parse(filePath);
        }

        return result;
    }
}
