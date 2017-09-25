package co.kr.sky.hymnbible.obj;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class MyServerListObj implements Parcelable{
	public static Creator<MyServerListObj> getCreator() {
		return CREATOR;
	}	
	String key_index;
	String name;
	String phone;
	String g_keyindex;
	int check;
	int copy_position;

	
	public MyServerListObj(String key_index, String name, String phone, String g_keyindex, int check, int copy_position) {
		super();
		this.key_index = key_index;
		this.name = name;
		this.phone = phone;
		this.g_keyindex = g_keyindex;
		this.check = check;
		this.copy_position = copy_position;

	}

	public int getCopy_position() {
		return copy_position;
	}

	public void setCopy_position(int copy_position) {
		this.copy_position = copy_position;
	}

	public String getKey_index() {
		return key_index;
	}

	public void setKey_index(String key_index) {
		this.key_index = key_index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getG_keyindex() {
		return g_keyindex;
	}

	public void setG_keyindex(String g_keyindex) {
		this.g_keyindex = g_keyindex;
	}

	public int getCheck() {
		return check;
	}

	public void setCheck(int check) {
		this.check = check;
	}

	public MyServerListObj(Parcel in) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key_index);
		dest.writeString(name);
		dest.writeString(phone);
		dest.writeString(g_keyindex);
		dest.writeInt(check);
		dest.writeInt(copy_position);
	}
	private void readFromParcel(Parcel in){
		key_index = in.readString();
		name = in.readString();
		phone = in.readString();
		g_keyindex = in.readString();
		check = in.readInt();
		copy_position = in.readInt();

	}
	@SuppressWarnings("rawtypes")
	public static final Creator<MyServerListObj> CREATOR = new Creator() {
		public Object createFromParcel(Parcel in) {
			return new MyServerListObj(in);
		}

		public Object[] newArray(int size) {
			return new MyServerListObj[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
