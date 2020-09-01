package com.theagriculture.app.Dda;

import java.util.ArrayList;

public class Section_DDA {
    public String sectionTitle;
    public ArrayList<String> Did;
    public ArrayList<String> Dlocation_name;
    public ArrayList<String> Dlocation_address;
    public ArrayList<String> Dlatitude;
    public ArrayList<String> Dlongitude;

    public ArrayList<String> DAdoID;
//    public String  DAdoID;
    public Boolean isPending,isCompleted,isOngoing;

    public Section_DDA(String sectionTitle, ArrayList<String> Did, ArrayList<String> Dlocation_name, ArrayList<String> Dlocation_address, ArrayList<String> Dlatitude, ArrayList<String> Dlongitude, Boolean isPending, Boolean isCompleted, Boolean isOngoing) {
        this.sectionTitle = sectionTitle;
        this.Did=Did;
        this.Dlocation_name = Dlocation_name;
        this.Dlocation_address = Dlocation_address;
        this.Dlatitude = Dlatitude;
        this.Dlongitude = Dlongitude;
        this.isPending = isPending;
        this.isCompleted = isCompleted;
        this.isOngoing = isOngoing;
    }

    public Section_DDA(String sectionTitle, ArrayList<String> Did, ArrayList<String> Dlocation_name, ArrayList<String> Dlocation_address, ArrayList<String> Dlatitude, ArrayList<String> Dlongitude,ArrayList<String> DAdoID/*String DAdoID*/, Boolean isPending, Boolean isCompleted, Boolean isOngoing) {
        this.sectionTitle = sectionTitle;
        this.Did=Did;
        this.Dlocation_name = Dlocation_name;
        this.Dlocation_address = Dlocation_address;
        this.Dlatitude = Dlatitude;
        this.Dlongitude = Dlongitude;
        this.isPending = isPending;
        this.isCompleted = isCompleted;
        this.isOngoing = isOngoing;
        this.DAdoID = DAdoID;

    }

//    public Section_DDA(ArrayList<String> DAdoID){
//        System.out.println("Hi dimple .... I am being called");
//        this.DAdoID = DAdoID;
//    }

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

    public Boolean getIsOngoing() {
        return isOngoing;
    }
    public void setIsOngoing(Boolean isOngoing) {
        this.isOngoing = isOngoing;
    }

    public ArrayList<String> getDAdoID() {
        return DAdoID;
    }

    public void setDAdoID(ArrayList<String> DAdoID) {
        this.DAdoID = DAdoID;
    }

}
