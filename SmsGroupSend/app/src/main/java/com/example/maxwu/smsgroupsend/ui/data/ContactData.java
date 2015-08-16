package com.example.maxwu.smsgroupsend.ui.data;

/**
 * Created by maxwu on 8/16/15.
 */
public class ContactData {
    private String name ;
    private String tel ;
    private boolean isChecked = false ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }
}
