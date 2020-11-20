package com.theagriculture.app.Admin;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private ArrayList<String> Date,adoName,ddaName;
    public String sectionTitle;

    //public List<String> allItemsInSection;
    public ArrayList<String> Dda,Ada,Address,Adapk,DdaPk,Id;
    //public List<Boolean> isComplete;
    Boolean isPending,isComplete,isOngoing;

    //variables for DDA User
    String sectionTitle_DDA;
    String villagename_DDA,blockname_DDA,district_DDA,state_DDA;
    public ArrayList<String> Id_DDA,address_DDA,name_DDA,mAdoIds_DDA;
    boolean is_DDA_user = false;

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

    //Constructor for DDA User
    public Section(String sectionTitle, ArrayList<String> id, String villagename, String blockname, String district, String state, ArrayList<String> address, ArrayList<String> name, ArrayList<String> mAdoIds,boolean is_DDA_user) {
        this.sectionTitle_DDA=sectionTitle;
//        this.sectionTitle=sectionTitle;
        this.Id_DDA=id;
        this.villagename_DDA=villagename;
        this.blockname_DDA=blockname;
        this.district_DDA=district;
        this.state_DDA=state;
        this.address_DDA=address;
        this.name_DDA=name;
        this.mAdoIds_DDA=mAdoIds;
        this.is_DDA_user=is_DDA_user;

    }

    public Section(String sectionTitle, ArrayList<String> mDid, ArrayList<String> mDlocation_name, ArrayList<String> mDlocation_address, ArrayList<String> mAdaName, ArrayList<String> mDdaName, ArrayList<String> Adapk,ArrayList<String> Ddapk, boolean isPending, boolean isCompleted, boolean isOngoing) {
        this.sectionTitle=sectionTitle;
        this.Ada=mAdaName;
        this.Dda=mDdaName;
        this.Address=mDlocation_address;
        this.Adapk=Adapk;
        this.DdaPk=Ddapk;
        this.isPending=isPending;
        this.isComplete= isCompleted;
        this.isOngoing=isOngoing;
        this.Id=mDid;
    }

    //Admin---Pending
    public Section(ArrayList<String> allDates, ArrayList<String> addressList, ArrayList<String> adoNameList, ArrayList<String> ddaNameList, boolean isPending, boolean isOngoing, boolean isCompleted) {
        this.Date=allDates;
        this.adoName=adoNameList;
        this.ddaName=ddaNameList;
        this.Address=addressList;
        this.isPending=isPending;
        this.isComplete= isCompleted;
        this.isOngoing=isOngoing;
    }


    public ArrayList<String> getDate() {
        return Date;
    }

    public void setDate(ArrayList<String> date) {
        Date = date;
    }

    public ArrayList<String> getAdoName() {
        return adoName;
    }

    public void setAdoName(ArrayList<String> adoName) {
        this.adoName = adoName;
    }

    public ArrayList<String> getDdaName() {
        return ddaName;
    }

    public void setDdaName(ArrayList<String> ddaName) {
        this.ddaName = ddaName;
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

    public boolean isIs_DDA_user() { return is_DDA_user; }
    public void setIs_DDA_user(boolean is_DDA_user) { this.is_DDA_user = is_DDA_user; }

    public String getSectionTitle_DDA() { return sectionTitle_DDA; }
    public void setSectionTitle_DDA(String sectionTitle_DDA) { this.sectionTitle_DDA = sectionTitle_DDA; }

    public String getVillagename_DDA() { return villagename_DDA; }
    public void setVillagename_DDA(String villagename_DDA) { this.villagename_DDA = villagename_DDA; }

    public String getBlockname_DDA() { return blockname_DDA; }
    public void setBlockname_DDA(String blockname_DDA) { this.blockname_DDA = blockname_DDA; }

    public String getDistrict_DDA() { return district_DDA; }
    public void setDistrict_DDA(String district_DDA) { this.district_DDA = district_DDA; }

    public String getState_DDA() { return state_DDA; }
    public void setState_DDA(String state_DDA) { this.state_DDA = state_DDA; }

    public ArrayList<String> getId_DDA() { return Id_DDA; }
    public void setId_DDA(ArrayList<String> id_DDA) { Id_DDA = id_DDA; }

    public ArrayList<String> getAddress_DDA() { return address_DDA; }
    public void setAddress_DDA(ArrayList<String> address_DDA) { this.address_DDA = address_DDA; }

    public ArrayList<String> getName_DDA() { return name_DDA; }
    public void setName_DDA(ArrayList<String> name_DDA) { this.name_DDA = name_DDA; }

    public ArrayList<String> getmAdoIds_DDA() { return mAdoIds_DDA; }
    public void setmAdoIds_DDA(ArrayList<String> mAdoIds_DDA) { this.mAdoIds_DDA = mAdoIds_DDA; }

}

