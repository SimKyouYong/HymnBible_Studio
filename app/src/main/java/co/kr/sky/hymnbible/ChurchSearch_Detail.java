package co.kr.sky.hymnbible;

import java.util.ArrayList;

import com.android.volley.toolbox.NetworkImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import co.kr.sky.hymnbible.net.H5ImageLoader;
import co.kr.sky.hymnbible.obj.ChurchObj;

public class ChurchSearch_Detail extends Activity{
	ChurchObj m_board;
	
	TextView title , church_name , church_type , person_name , church_address , church_number;
	TextView church_fax , church_homepage , church_body , church_post;

	TextView a1 , a2 ,a3 , a4 , a5 ,a6 ,a7 , a8, a9 , titlename;
	NetworkImageView i_img;
	private Typeface ttf;

	private int position = 0;
	private ArrayList<String> arr = new ArrayList<String>();
	Button bottomview_l,bottomview_r;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_church_detail);
		ttf = Typeface.createFromAsset(getAssets(), "HANYGO230.TTF");

		Bundle bundle = getIntent().getExtras();
		m_board = bundle.getParcelable("Object");

		//버튼 리스너 설정 
		findViewById(R.id.btn_back).setOnClickListener(btnListener);

		i_img 			= (NetworkImageView)findViewById(R.id.i_img);
		title 			= (TextView)findViewById(R.id.title);
		church_name 	= (TextView)findViewById(R.id.church_name);
		church_type 	= (TextView)findViewById(R.id.church_type);
		person_name 	= (TextView)findViewById(R.id.person_name);
		church_address  = (TextView)findViewById(R.id.church_address);
		church_number 	= (TextView)findViewById(R.id.church_number);
		church_fax 		= (TextView)findViewById(R.id.church_fax);
		church_homepage = (TextView)findViewById(R.id.church_homepage);
		church_body 	= (TextView)findViewById(R.id.church_body);
		titlename 		= (TextView)findViewById(R.id.titlename);
		church_post 		= (TextView)findViewById(R.id.church_post);
		bottomview_l 		= (Button)findViewById(R.id.bottomview_l);
		bottomview_r 		= (Button)findViewById(R.id.bottomview_r);
		
		a1 	= (TextView)findViewById(R.id.a1);
		a2 	= (TextView)findViewById(R.id.a2);
		a3 	= (TextView)findViewById(R.id.a3);
		a4 	= (TextView)findViewById(R.id.a4);
		a5 	= (TextView)findViewById(R.id.a5);
		a6 	= (TextView)findViewById(R.id.a6);
		a7 	= (TextView)findViewById(R.id.a7);
		a8 	= (TextView)findViewById(R.id.a8);
		a9 	= (TextView)findViewById(R.id.a9);

		a1.setTypeface(ttf);
		a2.setTypeface(ttf);
		a3.setTypeface(ttf);
		a4.setTypeface(ttf);
		a5.setTypeface(ttf);
		a6.setTypeface(ttf);
		a7.setTypeface(ttf);
		a8.setTypeface(ttf);
		a9.setTypeface(ttf);
		titlename.setTypeface(ttf);



		title.setTypeface(ttf);
		church_name.setTypeface(ttf);
		church_type.setTypeface(ttf);
		person_name.setTypeface(ttf);
		church_address.setTypeface(ttf);
		church_number.setTypeface(ttf);
		church_fax.setTypeface(ttf);
		church_homepage.setTypeface(ttf);
		church_body.setTypeface(ttf);
		church_post.setTypeface(ttf);
		bottomview_l.setTypeface(ttf);
		bottomview_r.setTypeface(ttf);


		titlename.setText("" 				+ "교회찾기/");
		title.setText("" 				+ m_board.getChurch_name());
		church_name.setText("" 			+ m_board.getChurch_name());
		church_type.setText("" 			+ m_board.getChurch_type());
		person_name.setText("" 			+ m_board.getPerson_name());
		church_address.setText("" 		+ m_board.getChurch_address());
		church_number.setText("" 		+ m_board.getChurch_number());
		church_fax.setText("" 			+ m_board.getChurch_fax());
		church_homepage.setText("" 		+ m_board.getChurch_homepage());
		church_body.setText("" 			+ m_board.getChurch_body());
		church_post.setText("" 			+ m_board.getChurch_post());
		Log.e("SKY", "우편번호  :: "+m_board.getChurch_post());

		Linkify.addLinks(church_number, Linkify.PHONE_NUMBERS);
		Linkify.addLinks(church_homepage, Linkify.WEB_URLS);

			
		//H5ImageLoader.getInstance(this).set( m_board.getChurch_img(), i_img);
		Log.e("SKY" , "URL :: " +  m_board.getChurch_img());
		H5ImageLoader.getInstance(this).set(m_board.getChurch_img(), i_img);
		findViewById(R.id.btn_back).setOnClickListener(btnListener);
		findViewById(R.id.bottomview_l).setOnClickListener(btnListener);
		findViewById(R.id.bottomview_r).setOnClickListener(btnListener);
		findViewById(R.id.btn_prev).setOnClickListener(btnListener);
		findViewById(R.id.btn_next).setOnClickListener(btnListener);

		if (!m_board.getChurch_img().equals("")) {
			arr.add(m_board.getChurch_img());
		}
		if (!m_board.getChurch_img2().equals("")) {
			arr.add(m_board.getChurch_img2());
		}
		if (!m_board.getChurch_img3().equals("")) {
			arr.add(m_board.getChurch_img3());
		}
		if (!m_board.getChurch_img4().equals("")) {
			arr.add(m_board.getChurch_img4());
		}
		if (!m_board.getChurch_img5().equals("")) {
			arr.add(m_board.getChurch_img5());
		}
		if (!m_board.getChurch_img6().equals("")) {
			arr.add(m_board.getChurch_img6());
		}
		if (!m_board.getChurch_img7().equals("")) {
			arr.add(m_board.getChurch_img7());
		}
		if (!m_board.getChurch_img8().equals("")) {
			arr.add(m_board.getChurch_img8());
		}
		if (!m_board.getChurch_img9().equals("")) {
			arr.add(m_board.getChurch_img9());
		}
		if (!m_board.getChurch_img10().equals("")) {
			arr.add(m_board.getChurch_img10());
		}
		Log.e("SKY", "arr size :: " + arr.size());
		if (arr.size() == 0) {
			i_img.setVisibility(View.GONE);
			((Button)findViewById(R.id.btn_next)).setVisibility(View.GONE);
			((Button)findViewById(R.id.btn_prev)).setVisibility(View.GONE);
		}
	}
	//버튼 리스너 구현 부분 
	View.OnClickListener btnListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_prev:	
				if (position == 0) {
					Toast.makeText(getApplicationContext(), "마지막 이미지 입니다.", 0).show();
				}else{
					position--;
					H5ImageLoader.getInstance(ChurchSearch_Detail.this).set(arr.get(position), i_img);
				}
				break;
			case R.id.btn_next:	
				if (position == (arr.size()-1)) {
					Toast.makeText(getApplicationContext(), "마지막 이미지 입니다.", 0).show();
				}else{
					position++;
					H5ImageLoader.getInstance(ChurchSearch_Detail.this).set(arr.get(position), i_img);
				}
				break;
			case R.id.bottomview_r:	
				Log.e("SKY"  , "--bottomview_r--");
				finish();
				break;
			case R.id.btn_back:	
				Log.e("SKY"  , "--btn_back--");
				finish();
				break;
			case R.id.bottomview_l:	
				Log.e("SKY"  , "--bottomview_l--");
				Intent it = new Intent(Intent.ACTION_SEND);
				it.setType("plain/text");
				 
				// 수신인 주소 - tos배열의 값을 늘릴 경우 다수의 수신자에게 발송됨
				String[] tos = { "sharp5200@naver.com" };
				it.putExtra(Intent.EXTRA_EMAIL, tos);
				it.putExtra(Intent.EXTRA_SUBJECT, m_board.getChurch_name() + "교회 정보수정 요청 드립니다.");
				it.putExtra(Intent.EXTRA_TEXT, "* 수정사항은 입력해주시고\n* 사진은 첨부해주시고\n\n교회명:\n교단명:\n담임목사:\n주소:\n우편번호:\n전화:\n팩스:\n홈페이지:\n교회소개: \n");
				startActivity(it);
				break;
			}
		}
	};
}
