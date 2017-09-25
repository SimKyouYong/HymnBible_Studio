package co.kr.sky.hymnbible.obj;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MyPhoneGroupObj implements Parcelable{
	public static Creator<MyPhoneGroupObj> getCreator() {
		return CREATOR;
	}	
	String _ID;
	String TITLE;
	String ACCOUNT_NAME;
	String ACCOUNT_TYPE;
	String DELETED;
	String GROUP_VISIBLE;
	String GROUP_COUNT;
	String CHECK;
	int    SELECTED;
	int    SELECTED_COUNT;
	
	public MyPhoneGroupObj(String _ID, String tITLE, String aCCOUNT_NAME, String aCCOUNT_TYPE, String dELETED,
			String gROUP_VISIBLE, String gROUP_COUNT, String cHECK, int SELECTED_, int SELECTED_COUNT_) {
		super();
		this._ID = _ID;
		TITLE = tITLE;
		ACCOUNT_NAME = aCCOUNT_NAME;
		ACCOUNT_TYPE = aCCOUNT_TYPE;
		DELETED = dELETED;
		GROUP_VISIBLE = gROUP_VISIBLE;
		GROUP_COUNT = gROUP_COUNT;
		CHECK = cHECK;
		SELECTED = SELECTED_;
		SELECTED_COUNT = SELECTED_COUNT_;
	}
	
	public int getSELECTED() {
		return SELECTED;
	}

	public void setSELECTED(int sELECTED) {
		SELECTED = sELECTED;
	}

	public int getSELECTED_COUNT() {
		return SELECTED_COUNT;
	}

	public void setSELECTED_COUNT(int sELECTED_COUNT) {
		SELECTED_COUNT = sELECTED_COUNT;
	}

	public String get_ID() {
		return _ID;
	}
	public void set_ID(String _ID) {
		this._ID = _ID;
	}
	public void set_Add_ID(String _ID) {
		this._ID = this._ID + "," + _ID;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}
	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}
	public String getACCOUNT_TYPE() {
		return ACCOUNT_TYPE;
	}
	public void setACCOUNT_TYPE(String aCCOUNT_TYPE) {
		ACCOUNT_TYPE = aCCOUNT_TYPE;
	}
	public String getDELETED() {
		return DELETED;
	}
	public void setDELETED(String dELETED) {
		DELETED = dELETED;
	}
	public String getGROUP_VISIBLE() {
		return GROUP_VISIBLE;
	}
	public void setGROUP_VISIBLE(String gROUP_VISIBLE) {
		GROUP_VISIBLE = gROUP_VISIBLE;
	}
	public String getGROUP_COUNT() {
		return GROUP_COUNT;
	}
	public void setGROUP_COUNT(String gROUP_COUNT) {
		GROUP_COUNT = gROUP_COUNT;
	}
	public void set_Add_GROUP_COUNT(int gROUP_COUNT) {
		GROUP_COUNT = ""+ (Integer.parseInt(GROUP_COUNT) + gROUP_COUNT);
	}
	public String getCHECK() {
		return CHECK;
	}
	public void setCHECK(String cHECK) {
		CHECK = cHECK;
	}
	public MyPhoneGroupObj(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(_ID);
		dest.writeString(TITLE);
		dest.writeString(ACCOUNT_NAME);
		dest.writeString(ACCOUNT_TYPE);
		dest.writeString(DELETED);
		dest.writeString(GROUP_VISIBLE);
		dest.writeString(GROUP_COUNT);
		dest.writeString(CHECK);
		dest.writeInt(SELECTED);
		dest.writeInt(SELECTED_COUNT);

		
		
	}
	private void readFromParcel(Parcel in){
		_ID = in.readString();
		TITLE = in.readString();
		ACCOUNT_NAME = in.readString();
		ACCOUNT_TYPE = in.readString();
		DELETED = in.readString();
		GROUP_VISIBLE = in.readString();
		GROUP_COUNT = in.readString();
		CHECK = in.readString();
		SELECTED = in.readInt();
		SELECTED_COUNT = in.readInt();

	}
	@SuppressWarnings("rawtypes")
	public static final Creator<MyPhoneGroupObj> CREATOR = new Creator() {
		public Object createFromParcel(Parcel in) {
			return new MyPhoneGroupObj(in);
		}

		public Object[] newArray(int size) {
			return new MyPhoneGroupObj[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
