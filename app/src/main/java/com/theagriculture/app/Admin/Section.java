package com.theagriculture.app.Admin;

import java.util.ArrayList;
import java.util.List;

public class Section {
    public String sectionTitle;

    //public List<String> allItemsInSection;
    public ArrayList<String> Dda,Ada,Address,Adapk,DdaPk,Id;
    //public List<Boolean> isComplete;
    Boolean isPending,isComplete,isOngoing;
    public Section(String sectionTitle, ArrayList<String> Dda,ArrayList<String> Ada,ArrayList<String> Address,ArrayList<String> Id,ArrayList<String> Adapk,ArrayList<String> Ddapk, Boolean isPending,Boolean isOngoing,Boolean isComplete){
        this.sectionTitle=sectionTitle;
        this.Ada=Ada;
        this.Dda=Dda;
        this.Address=Address;
        this.Adapk=Adapk;
        this.DdaPk=Ddapk;
        this.isPending=isPending;
        this.isComplete= isComplete;
        this.isOngoing=isOngoing;
        this.Id=Id;

    }
    /*
    public Section(String sectionTitle, List<String> allItemsInSection) {
        this.sectionTitle = sectionTitle;
        this.allItemsInSection = allItemsInSection;
    }
    */
    public String getSectionTitle() {
        return sectionTitle;
    }
    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }
    public ArrayList<String> getAda() {
        return Ada;
    }
    public void setAda(ArrayList<String> Ada) {
        this.Ada = Ada;
    }
    public ArrayList<String> getDda() {
        return Dda;
    }
    public void setDda(ArrayList<String> Dda) {
        this.Dda=Dda;
    }
    public ArrayList<String> getAddress() {
        return Address;
    }
    public void setAddress(ArrayList<String> Address) {
        this.Address = Address;
    }
    public ArrayList<String> getId() { return Id; }
    public void setId(ArrayList<String> Id) {
        this.Id = Id;
    }
    public ArrayList<String> getAdapk() {
        return Adapk;
    }
    public void setAdapk(ArrayList<String> Adapk) {
        this.Adapk = Adapk;
    }
    public ArrayList<String> getDdapk() { return DdaPk; }
    public void setDdapk(ArrayList<String> Ddapk) {
        this.DdaPk = Ddapk;
    }
    public boolean getPendingstatus() {
        return isPending;
    }
    public void setPendingstatus(Boolean isPending) {
        this.isPending = isPending;
    }
    public boolean getOngoingstatus() {
        return isOngoing;
    }
    public void setOngoingstatus(Boolean isOngoing) {
        this.isOngoing = isOngoing;
    }
    public boolean getCompletedstatus() {
        return isComplete;
    }
    public void setCompletedstatus(Boolean isComplete) {
        this.isComplete = isComplete;
    }
    /*
    public List<String> getAllItemsInSection() {
        return allItemsInSection;
    }
    public void setAllItemsInSection(List<String> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

     */
}
