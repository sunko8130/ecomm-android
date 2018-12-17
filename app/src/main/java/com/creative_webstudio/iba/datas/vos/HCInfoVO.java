package com.creative_webstudio.iba.datas.vos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HCInfoVO implements Parcelable {

    private double id = 0;

    private String title = "";

    private String image="";

    @SerializedName("short-description")
    private String shortDec="";


    @SerializedName("published-date")
    private String pubDate="";

    @SerializedName("complete-url")
    private String url="";

    protected HCInfoVO(Parcel in) {
        id = in.readDouble();
        title = in.readString();
        image = in.readString();
        shortDec = in.readString();
        pubDate = in.readString();
        url = in.readString();
    }

    public static final Creator<HCInfoVO> CREATOR = new Creator<HCInfoVO>() {
        @Override
        public HCInfoVO createFromParcel(Parcel in) {
            return new HCInfoVO(in);
        }

        @Override
        public HCInfoVO[] newArray(int size) {
            return new HCInfoVO[size];
        }
    };

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShortDec() {
        return shortDec;
    }

    public void setShortDec(String shortDec) {
        this.shortDec = shortDec;
    }



    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(shortDec);
        dest.writeString(pubDate);
        dest.writeString(url);
    }
}
