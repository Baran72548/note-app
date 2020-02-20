package com.barmej.mynote.data;

import java.io.Serializable;
import java.util.ArrayList;

public class CheckItem implements Serializable {
    private String checkBoxItemText;
    private boolean checkBoxItemStatus;

    public CheckItem(String checkBoxItemText, boolean checkBoxItemStatus) {
        this.checkBoxItemText = checkBoxItemText;
        this.checkBoxItemStatus = checkBoxItemStatus;
    }

    public String getCheckBoxItemText() {
        return checkBoxItemText;
    }

    public boolean getCheckBoxItemStatus() {
        return checkBoxItemStatus;
    }

    public void setCheckBoxItemStatus(boolean checkBoxItemStatus) {
        this.checkBoxItemStatus = checkBoxItemStatus;
    }

    public static ArrayList<CheckItem> getChecklistList() {
        ArrayList<CheckItem> checklistItem = new ArrayList<>();
        return checklistItem;
    }
}
