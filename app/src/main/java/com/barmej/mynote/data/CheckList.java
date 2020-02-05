package com.barmej.mynote.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;

import java.util.ArrayList;

public class CheckList implements Parcelable {
    private String checkListItemText;
    private boolean checkListItemStatus;
    private CheckBox checkBoxStatus;

    public CheckList(String checkListItemText, CheckBox checkBoxStatus) {
        this.checkListItemText = checkListItemText;
        this.checkBoxStatus = checkBoxStatus;
    }

    public CheckList(String checkListItemText, boolean checkListItemStatus) {
        this.checkListItemText = checkListItemText;
        this.checkListItemStatus = checkListItemStatus;
    }

    public String getCheckListItemText() {
        return checkListItemText;
    }

    public static ArrayList<CheckList> getChecklistList() {
        ArrayList<CheckList> checklistList = new ArrayList<>();
        return checklistList;
    }

    public boolean isCheckListItemStatus() {
        return checkListItemStatus;
    }

    public void setCheckListItemStatus(boolean checkListItemStatus) {
        this.checkListItemStatus = checkListItemStatus;
    }

    //write object values to parcel for storage
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(checkListItemText);
    }

    //constructor used for parcel
    public CheckList(Parcel parcel) {
        checkListItemText = parcel.readString();
    }

    public static final Parcelable.Creator<CheckList> CREATOR = new Parcelable.Creator<CheckList>() {
        @Override
        public CheckList createFromParcel(Parcel parcel) {
            return new CheckList(parcel);
        }

        @Override
        public CheckList[] newArray(int size) {
            return new CheckList[size];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }
}
