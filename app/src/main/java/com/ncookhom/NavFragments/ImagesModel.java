package com.ncookhom.NavFragments;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ma7MouD on 4/9/2018.
 */

public class ImagesModel implements Parcelable{

    private String img1, img2, img3, img4, img5 ;

    public ImagesModel(String img1, String img2, String img3, String img4, String img5) {
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.img4 = img4;
        this.img5 = img5;
    }

    protected ImagesModel(Parcel in) {
        img1 = in.readString();
        img2 = in.readString();
        img3 = in.readString();
        img4 = in.readString();
        img5 = in.readString();
    }

    public static final Creator<ImagesModel> CREATOR = new Creator<ImagesModel>() {
        @Override
        public ImagesModel createFromParcel(Parcel in) {
            return new ImagesModel(in);
        }

        @Override
        public ImagesModel[] newArray(int size) {
            return new ImagesModel[size];
        }
    };

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getImg4() {
        return img4;
    }

    public void setImg4(String img4) {
        this.img4 = img4;
    }

    public String getImg5() {
        return img5;
    }

    public void setImg5(String img5) {
        this.img5 = img5;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(img1);
        parcel.writeString(img2);
        parcel.writeString(img3);
        parcel.writeString(img4);
        parcel.writeString(img5);
    }
}
