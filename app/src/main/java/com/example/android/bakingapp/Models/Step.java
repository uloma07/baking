package com.example.android.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {

    Long id;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;

    public String getshortDescription() {
        return this.shortDescription;
    }
    public void setshortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String Description) {
        this.description = Description;
    }

    public String getvideoURL() {
        return this.videoURL;
    }
    public void setvideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getthumbnailURL() {
        return this.thumbnailURL;
    }
    public void setthumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public Long getId() {
        return this.id;
    }
    public void setID(Long ID) {
        this.id = ID;
    }

    public Step(Parcel in ) {
        readFromParcel( in );
    }

    public Step(){ }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Step createFromParcel(Parcel in ) {
            return new Step( in );
        }

        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeLong(id);

    }

    private void readFromParcel(Parcel in) {
        videoURL = in.readString();
        thumbnailURL = in.readString();
        shortDescription = in.readString();
        description = in.readString();
        id = in.readLong();
    }

    /*

    {
        "id": 0,
        "shortDescription": "Recipe Introduction",
        "description": "Recipe Introduction",
        "videoURL": "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
        "thumbnailURL": ""
      }

     */

}
