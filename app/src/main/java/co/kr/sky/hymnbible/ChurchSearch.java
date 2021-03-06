package co.kr.sky.hymnbible;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import co.kr.sky.AccumThread;
import co.kr.sky.hymnbible.adapter.ChurchSearch_Adapter;
import co.kr.sky.hymnbible.fun.CommonUtil;
import co.kr.sky.hymnbible.obj.ChurchObj;


public class ChurchSearch extends Activity
        implements OnMapReadyCallback {
    private GoogleMap mMap;

    LinearLayout list_view_11;
    EditText e_search1;
    TextView list_count;
    private ListView                m_ListView;
    private Typeface ttf;
    protected ProgressDialog customDialog = null;
    Map<String, String> map = new HashMap<String, String>();
    AccumThread mThread;
    CommonUtil dataSet = CommonUtil.getInstance();
    String [][]Object_Array;
    ArrayList<ChurchObj> arrData = new ArrayList<ChurchObj>();
    private ArrayList<Bitmap> bm = new ArrayList<Bitmap>();
    private ChurchSearch_Adapter m_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church);
        ttf = Typeface.createFromAsset(getAssets(), "HANYGO230.TTF");

        e_search1 = (EditText)findViewById(R.id.e_search1);
        m_ListView = (ListView)findViewById(R.id.list_cummun);
        list_view_11 = (LinearLayout)findViewById(R.id.list_view_11);
        list_count = (TextView)findViewById(R.id.list_count);

        //e_search1.setOnEditorActionListener(ChurchSearch.this); //mEditText와 onEditorActionListener를 연결


        list_count.setTypeface(ttf);
        e_search1.setTypeface(ttf);
        findViewById(R.id.btn_back).setOnClickListener(btnListener);
        findViewById(R.id.btn_sp2).setOnClickListener(btnListener);
        findViewById(R.id.btn_sp3).setOnClickListener(btnListener);
        findViewById(R.id.url_send).setOnClickListener(btnListener);
        e_search1.setText("");

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);


    }
    //버튼 리스너 구현 부분
    View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {


                case R.id.url_send:
                    //지훈이 url 나오면 SubWEBVIEW 에 넘기기..
                    Intent board = new Intent(ChurchSearch.this , ChurchSearchWebvie.class);
                    startActivity(board);
                    break;
                case R.id.btn_sp2:
                    SendHttp();
                    break;
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.btn_sp3:
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"교회명.담임목사.주소.전화등 말씀해주세요");
                    try {
                        startActivityForResult(intent, MainActivity.REQ_CODE_SPEECH_INPUT);
                    } catch (ActivityNotFoundException a) {
                        Toast.makeText(ChurchSearch.this,"다시 시도해주세요.",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };
    Handler mAfterAccum = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.arg1  == 0 ) {
                bm.clear();
                arrData.clear();
                String res = (String)msg.obj;
                Log.e("SKY", "RES :: " +  res);
                try {
                    JSONArray ja = new JSONArray(res);
                    if (ja.length() == 0 ) {
                        Toast.makeText(getApplicationContext(), "해당 검색 데이터는 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        //지도 꽉차게 표시
                        list_view_11.setVisibility(View.GONE);
                        //mMap.clear();
                        customProgressClose();
                        return;
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        bm.add(null);
                        JSONObject order = ja.getJSONObject(i);
                        String key_index = order.getString("key_index");
                        String church_name = order.getString("church_name");
                        String church_type = order.getString("church_type");
                        String person_name = order.getString("person_name");
                        String church_post = order.getString("church_post");
                        String church_address = order.getString("church_address");
                        String church_number = order.getString("church_number");
                        String church_fax = order.getString("church_fax");
                        String church_homepage = order.getString("church_homepage");
                        String church_body  = order.getString("church_body");
                        String church_img = order.getString("church_img");
                        String church_img2 = order.getString("church_img2");
                        String church_img3 = order.getString("church_img3");
                        String church_img4 = order.getString("church_img4");
                        String church_img5 = order.getString("church_img5");
                        String church_img6 = order.getString("church_img6");
                        String church_img7 = order.getString("church_img7");
                        String church_img8 = order.getString("church_img8");
                        String church_img9 = order.getString("church_img9");
                        String church_img10 = order.getString("church_img10");
                        String search_index = order.getString("search_index");
                        String latitude = order.getString("latitude");
                        String hardness = order.getString("hardness");
                        String VIEWING = order.getString("VIEWING");
                        //latitude1 = "" + latitude.substring(0 , 2) + "." + latitude.substring(2,latitude.length());
                        //hardness1 = "" + hardness.substring(0 , 3) + "." + hardness.substring(3,hardness.length());
                        if(VIEWING.trim().equals("1")){
                            arrData.add(new ChurchObj(key_index,
                                    church_name,
                                    church_type,
                                    person_name,
                                    church_post,
                                    church_address,
                                    church_number,
                                    church_fax,
                                    church_homepage,
                                    church_body,
                                    church_img,
                                    church_img2,
                                    church_img3,
                                    church_img4,
                                    church_img5,
                                    church_img6,
                                    church_img7,
                                    church_img8,
                                    church_img9,
                                    church_img10,
                                    search_index,
                                    latitude,
                                    hardness));
                        }

                    }
                    //write(arrData);
//					write("" + key_index + "/" + church_address + "/" + latitude + "/" + hardness);
                    list_view_11.setVisibility(View.VISIBLE);
                    list_count.setText("\""+e_search1.getText().toString() +"\"" + " 검색 결과는 총 " + arrData.size() + " 건 입니다.");
                    m_Adapter = new ChurchSearch_Adapter( ChurchSearch.this , arrData);
                    m_ListView.setOnItemClickListener(mItemClickListener);
                    m_ListView.setAdapter(m_Adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                customProgressClose();
                MapMarker();
            }else if(msg.arg1 == 2){
                //mapMarker(msg.arg2);
            }
        }
    };
    private void SendHttp(){
        if (e_search1.getText().toString().length() ==0) {
            Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(e_search1.getWindowToken(), 0);
        recycleImages();
        customProgressPop();
        //String []val = {"KEY_INDEX" , "CODE","NAME","ADDRESS",
        //		"LOAD_ADDRESS","PHONE","LATITUDE", "HARDNESS","FILE_NAME","PLACE","INTRODUCE"};
        String []val = {};
        map.clear();
        map.put("url", dataSet.SERVER + "searchChurchJson.do");
        map.put("type", e_search1.getText().toString());
        mThread = new AccumThread(ChurchSearch.this , mAfterAccum , map , 0 , 0 , null);

        mThread.start();		//스레드 시작!!
    }
    private void Allpin(int i){
        if (!arrData.get(i).getLatitude().equals("")  && !arrData.get(i).getLongitude().equals("")) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("First Location");
            markerOptions.snippet("This Is Test Location");
            LatLng latlng = null;
            latlng = new LatLng(Double.parseDouble(arrData.get(i).getLatitude()), Double.parseDouble(arrData.get(i).getLongitude()));
            markerOptions.position(latlng);

            mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(""+i)
                    .snippet(arrData.get(i).getChurch_name())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.gps_experience)));

            if (arrData.get(i).getChurch_img().length() > 0) {
                Log.e("SKY" , "MapMarker : ");
                AccumThread1 av = new AccumThread1(arrData.get(i).getChurch_img(), i );
                av.start();

            }
        }
    }
    public class AccumThread1 extends Thread{
        String url_;
        int i_;
        ImageView a_;
        public AccumThread1(String url, int i){
            this.url_ = url;
            this.i_ = i;
        }
        public AccumThread1(String url, int i , ImageView a){
            this.url_ = url;
            this.i_ = i;
            this.a_ = a;
        }
        @Override
        public void run()
        {
            bm.set(i_, getBitmapFromURL(url_));
        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        Log.e("SKY" , "HTTP : " + src);
        if (src.length() == 0) {
            Log.e("SKY" , "src.length() 00 : ");

            return null;
        }
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap_resize = BitmapFactory.decodeStream(input,null, options);
            return bitmap_resize;
        } catch (IOException e) {
            // Log exception
            Log.e("SKY", "ERROR :: " + e.toString());
            return null;
        }
    }
    private void MapMarker(){
        mMap.clear();
        //mMap.setMyLocationEnabled(true);
        for (int i = 0; i < arrData.size(); i++) {
            Allpin(i);
        }
        Log.e("SKY" , "MapMarker : ");

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter() {
            @Override
            public View getInfoContents(Marker marker) {
				/*
				View myContentView = getLayoutInflater().inflate(
						R.layout.custommarker, null);
				ImageView img = (ImageView) myContentView.findViewById(R.id.img_c);
				marker.getId();
				int index = Integer.parseInt(marker.getTitle());
				Log.e("SKY" , "index ::" + index);
				//				String[] array = dataSet.Token_Str(arrData.get(index).getFILE_NAME());
				img.setImageBitmap(bm.get(index));
				TextView tvTitle = ((TextView) myContentView.findViewById(R.id.title));
				tvTitle.setText("주소: "+arrData.get(index).getChurch_address());
				TextView tvSnippet = ((TextView) myContentView.findViewById(R.id.snippet));
				tvSnippet.setText("▶ "+arrData.get(index).getChurch_name());
				TextView name = ((TextView) myContentView.findViewById(R.id.name));
				name.setText("담임목사:"+arrData.get(index).getPerson_name());
				myContentView.setBackgroundColor(Color.WHITE);
				*/
                return null;
            }
        });



        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                // TODO Auto-generated method stub
                Log.e("SKY" , "-- onInfoWindowClick --");
                int index = Integer.parseInt(arg0.getTitle());

                Intent board = new Intent(ChurchSearch.this , ChurchSearch_Detail.class);
                board.putExtra("Object", arrData.get(index));
                startActivity(board);
            }
        });
        if (arrData.size() > 1) {



            //딜레이
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(36.3370879,127.5477928));
                    mMap.moveCamera(update);		//자기 위치로 이동
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(6);
                    mMap.animateCamera(zoom);
                }
            }, 500);// 0.5초 정도 딜레이를 준 후 시작
            return;
        }

        //딜레이
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(arrData.get(0).getLatitude()), Double.parseDouble(arrData.get(0).getLongitude())));
                mMap.moveCamera(update);		//자기 위치로 이동
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                mMap.animateCamera(zoom);
            }
        }, 500);// 0.5초 정도 딜레이를 준 후 시작


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("CHECK2", "onDestroy");
        try {
            recycleImages();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    /** 메모리에 남아있는 이미지 제거 */
    private void recycleImages() {
        if (bm == null) {
            return;
        }
        for (Bitmap bm : this.bm) {
            if (bm != null) {
                bm.recycle();
                bm = null;
                Log.e("CHECK2", "bm bye..");
            }
        }
        bm.clear();
    }
    public void customProgressPop(){
        try{
            if (customDialog==null){
                customDialog = new ProgressDialog( this );
            }
            customDialog.show();
        }catch(Exception ex){}
    }
    public void customProgressClose(){
        if (customDialog!=null && customDialog.isShowing()){
            try{
                customDialog.cancel();
                customDialog.dismiss();
                customDialog = null;
            }catch(Exception e)
            {}
        }
    }
    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position,
                                long id) {
            Intent board = new Intent(ChurchSearch.this, ChurchSearch_Detail.class);
            board.putExtra("Object", arrData.get(position));
            startActivity(board);
        }
    };
    private class CustomInfoWindowAdapter implements InfoWindowAdapter {
        private View view;
        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custommarker,
                    null);
        }
        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {

            View myContentView = getLayoutInflater().inflate(
                    R.layout.custommarker, null);
            //NetworkImageView img = ((NetworkImageView) myContentView.findViewById(R.id.img_c));
            ImageView img = (ImageView) myContentView.findViewById(R.id.img_c);
            marker.getId();
            int index = Integer.parseInt(marker.getTitle());
            //			String[] array = dataSet.Token_Str(mData.get(index).getFILE_NAME());
            img.setImageBitmap(bm.get(index));
            TextView tvTitle = ((TextView) myContentView.findViewById(R.id.title));
            tvTitle.setText("주소: "+arrData.get(index).getChurch_address());
            TextView tvSnippet = ((TextView) myContentView.findViewById(R.id.snippet));
            tvSnippet.setText("▶ "+arrData.get(index).getChurch_name());
            TextView name = ((TextView) myContentView.findViewById(R.id.name));
            name.setText("담임목사:"+arrData.get(index).getPerson_name());
            myContentView.setBackgroundColor(Color.WHITE);
            return myContentView;
        }
    }
    @Override
    public void onMapReady(final GoogleMap map) {
        mMap = map;

        LatLng SEOUL = new LatLng(36.397955, 127.784735);

        /*
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        map.addMarker(markerOptions);

        */
        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(7));
    }


}
