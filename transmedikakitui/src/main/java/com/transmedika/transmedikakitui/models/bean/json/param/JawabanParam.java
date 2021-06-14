package com.transmedika.transmedikakitui.models.bean.json.param;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class JawabanParam implements Parcelable {

	@SerializedName("question")
	private String question;

	@SerializedName("answer")
	private String answer;

	@SerializedName("id")
	private String id;

	public JawabanParam() {
	}

	protected JawabanParam(Parcel in) {
		question = in.readString();
		answer = in.readString();
		id = in.readString();
	}

	public static final Creator<JawabanParam> CREATOR = new Creator<JawabanParam>() {
		@Override
		public JawabanParam createFromParcel(Parcel in) {
			return new JawabanParam(in);
		}

		@Override
		public JawabanParam[] newArray(int size) {
			return new JawabanParam[size];
		}
	};

	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return question;
	}

	public void setAnswer(String answer){
		this.answer = answer;
	}

	public String getAnswer(){
		return answer;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof JawabanParam)) return false;
		JawabanParam that = (JawabanParam) o;
		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(question);
		dest.writeString(answer);
		dest.writeString(id);
	}
}