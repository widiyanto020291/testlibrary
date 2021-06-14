package com.transmedika.transmedikakitui.models.bean.json;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Clinic implements Parcelable {

	@SerializedName("image")
	private String image;

	@SerializedName("address")
	private String address;

	@SerializedName("province")
	private String province;

	@SerializedName("regency_id")
	private Integer regencyId;

	@SerializedName("province_id")
	private Integer provinceId;

	@SerializedName("map_lng")
	private String mapLng;

	@SerializedName("name")
	private String name;

	@SerializedName("map_lat")
	private String mapLat;

	@SerializedName("regency")
	private String regency;

	@SerializedName("id")
	private Long id;

	public Clinic() {
	}

	public Clinic(Long id) {
		this.id = id;
	}

	protected Clinic(Parcel in) {
		image = in.readString();
		address = in.readString();
		province = in.readString();
		if (in.readByte() == 0) {
			regencyId = null;
		} else {
			regencyId = in.readInt();
		}
		if (in.readByte() == 0) {
			provinceId = null;
		} else {
			provinceId = in.readInt();
		}
		mapLng = in.readString();
		name = in.readString();
		mapLat = in.readString();
		regency = in.readString();
		if (in.readByte() == 0) {
			id = null;
		} else {
			id = in.readLong();
		}
	}

	public static final Creator<Clinic> CREATOR = new Creator<Clinic>() {
		@Override
		public Clinic createFromParcel(Parcel in) {
			return new Clinic(in);
		}

		@Override
		public Clinic[] newArray(int size) {
			return new Clinic[size];
		}
	};

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setProvince(String province){
		this.province = province;
	}

	public String getProvince(){
		return province;
	}

	public void setRegencyId(Integer regencyId){
		this.regencyId = regencyId;
	}

	public Integer getRegencyId(){
		return regencyId;
	}

	public void setProvinceId(Integer provinceId){
		this.provinceId = provinceId;
	}

	public Integer getProvinceId(){
		return provinceId;
	}

	public void setMapLng(String mapLng){
		this.mapLng = mapLng;
	}

	public String getMapLng(){
		return mapLng;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMapLat(String mapLat){
		this.mapLat = mapLat;
	}

	public String getMapLat(){
		return mapLat;
	}

	public void setRegency(String regency){
		this.regency = regency;
	}

	public String getRegency(){
		return regency;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getId(){
		return id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(image);
		dest.writeString(address);
		dest.writeString(province);
		if (regencyId == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(regencyId);
		}
		if (provinceId == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeInt(provinceId);
		}
		dest.writeString(mapLng);
		dest.writeString(name);
		dest.writeString(mapLat);
		dest.writeString(regency);
		if (id == null) {
			dest.writeByte((byte) 0);
		} else {
			dest.writeByte((byte) 1);
			dest.writeLong(id);
		}
	}
}