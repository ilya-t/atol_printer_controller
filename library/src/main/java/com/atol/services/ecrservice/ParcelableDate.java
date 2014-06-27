package com.atol.services.ecrservice;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ParcelableDate extends Date implements Parcelable 
 {
	static final long serialVersionUID = 1L;
	public static final Creator<ParcelableDate> CREATOR =
			new Creator<ParcelableDate>()
		{
			public ParcelableDate createFromParcel(Parcel in) {
				return new ParcelableDate(in.readLong());
			}

			public ParcelableDate[] newArray(int arg0) {
				return new ParcelableDate[arg0];
			}
	};

	public ParcelableDate(long date) {
		super(date);
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(getTime());
	}
} 