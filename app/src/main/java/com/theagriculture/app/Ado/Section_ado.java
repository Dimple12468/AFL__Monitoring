package com.theagriculture.app.Ado;

import java.util.ArrayList;

public class Section_ado {
    public String sectionTitle;
    public ArrayList<String> Did,Dlocation_name,Dlocation_address,Dlatitude,Dlongitude;
    public Boolean isPending,isCompleted;

    public Section_ado(String sectionTitle, ArrayList<String> Did, ArrayList<String> Dlocation_name, ArrayList<String> Dlocation_address, ArrayList<String> Dlatitude, ArrayList<String> Dlongitude, Boolean isPending, Boolean isCompleted) {
        this.sectionTitle = sectionTitle;
        this.Did=Did;
        this.Dlocation_name = Dlocation_name;
        this.Dlocation_address = Dlocation_address;
        this.Dlatitude = Dlatitude;
        this.Dlongitude = Dlongitude;
        this.isPending = isPending;
        this.isCompleted = isCompleted;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }
    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public ArrayList<String> getDid() {
        return Did;
    }
    public void setDid(ArrayList<String> Did) {
        this.Did = Did;
    }

    public ArrayList<String> getDlocation_name() {
        return Dlocation_name;
    }
    public void setDlocation_name(ArrayList<String> Dlocation_name) {
        this.Dlocation_name = Dlocation_name;
    }

    public ArrayList<String> getDlocation_address() {
        return Dlocation_address;
    }
    public void setDlocation_address(ArrayList<String> Dlocation_address) {
        this.Dlocation_address = Dlocation_address;
    }

    public ArrayList<String> getDlatitude() {
        return Dlatitude;
    }
    public void setDlatitude(ArrayList<String> Dlatitude) {
        this.Dlatitude = Dlatitude;
    }

    public ArrayList<String> getDlongitude() {
        return Dlongitude;
    }
    public void setDlongitude(ArrayList<String> Dlongitude) {
        this.Dlongitude = Dlongitude;
    }

    public Boolean getIsPending() {
        return isPending;
    }
    public void setIsPending(Boolean isPending) {
        this.isPending = isPending;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
