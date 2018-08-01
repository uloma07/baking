package com.example.android.bakingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    float quantity;
    String measure;
    String ingredient;

    public float getquantity() {
        return this.quantity;
    }
    public void setquantity(float quantity) {
        this.quantity = quantity;
    }

    public String getmeasure() {
        return this.measure;
    }
    public void setmeasure(String measure) {
        this.measure = measure;
    }

    public String getingredient() {
        return this.ingredient;
    }
    public void setingredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Ingredient(Parcel in ) {
        readFromParcel( in );
    }

    public Ingredient(){ }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ingredient createFromParcel(Parcel in ) {
            return new Ingredient( in );
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredient);
        dest.writeString(measure);
        dest.writeFloat(quantity);
    }

    private void readFromParcel(Parcel in) {
        ingredient = in.readString();
        measure = in.readString();
        quantity = in.readFloat();
    }

    /*

    "quantity": 2,
        "measure": "CUP",
        "ingredient": "Graham Cracker crumbs"

     */

}
