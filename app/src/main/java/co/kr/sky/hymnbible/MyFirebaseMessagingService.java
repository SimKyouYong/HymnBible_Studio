package co.kr.sky.hymnbible;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import java.net.URL;


public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    Bitmap bigPicture;

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //remoteMessage.getData().get("msg")
        //추가한것 - 메세지와 이미지를 인수로 지정
        sendNotification(remoteMessage.getData().get("msg"), remoteMessage.getData().get("imgurllink"));
        //이미지 링크가 제대로 들어 있나 콘솔에 찍어 봄
        Log.e("SKY", "imgurl: " + remoteMessage.getData().get("imgurllink"));
        Log.e("SKY", "msg: " + remoteMessage.getData().get("msg"));

    }

    private void sendNotification(String messageBody, String myimgurl) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //이미지 온라인 링크를 가져와 비트맵으로 바꾼다.
        if (!myimgurl.equals("")){
            try {
                URL url = new URL("http://shqrp5200.cafe24.com/upload/"+myimgurl);
                bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!myimgurl.equals("")){
            //이미지 있을때.
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageBody)
                    .setContentText("알림탭을 아래로 천천히 드래그 하세요.")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)


                    //이미지를 보내는 스타일 사용하기
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bigPicture)
                            .setBigContentTitle("상세 내용")
                            .setSummaryText(messageBody))

                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }else{
            //이미지 없을때.
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageBody)
                    .setContentText("알림탭을 아래로 천천히 드래그 하세요.")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)

                //BigTextStyle
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setBigContentTitle("상세 내용")
                        .bigText(messageBody))

                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }



        //추가한것
        //sendNotification(remoteMessage.getData().get("msg"));
    }
    /*
    private void sendNotification(String messageBody) {
        StringTokenizer token = new StringTokenizer(messageBody, "||");
        String title = token.nextToken();
        String message = token.nextToken();
        String url = token.nextToken();

        Log.e("SKY" , "URL :: " + url);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("url" , url);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());
    }
    */
}

