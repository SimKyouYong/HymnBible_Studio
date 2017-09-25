package co.kr.sky.hymnbible.obj;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MyPhoneListObj implements Parcelable{
	public static Creator<MyPhoneListObj> getCreator() {
		return CREATOR;
	}	
	String key;
	String NAME;
	String PHONE;
	int CHECK;
	int copy_position;
	
	public MyPhoneListObj(String key,String nAME, String pHONE, int cHECK, int copy_position) {
		super();
		this.key = key;
		NAME = nAME;
		PHONE = pHONE;
		CHECK = cHECK;
		this.copy_position = copy_position;
	}
	
	
	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public int getCHECK() {
		return CHECK;
	}
	public void setCHECK(int cHECK) {
		CHECK = cHECK;
	}
	public MyPhoneListObj(Parcel in) {
		readFromParcel(in);
	}
	public int getCopy_position() {
		return copy_position;
	}
	public void setCopy_position(int copy_position) {
		this.copy_position = copy_position;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key);
		dest.writeString(NAME);
		dest.writeString(PHONE);
		dest.writeInt(CHECK);
		dest.writeInt(copy_position);

	}
	private void readFromParcel(Parcel in){
		key = in.readString();
		NAME = in.readString();
		PHONE = in.readString();
		CHECK = in.readInt();
		copy_position = in.readInt();

	}
	@SuppressWarnings("rawtypes")
	public static final Creator<MyPhoneListObj> CREATOR = new Creator() {
		public Object createFromParcel(Parcel in) {
			return new MyPhoneListObj(in);
		}

		public Object[] newArray(int size) {
			return new MyPhoneListObj[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
