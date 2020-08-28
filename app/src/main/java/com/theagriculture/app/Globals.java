package com.theagriculture.app;

public class Globals {

    public static String serverURL = "http://api.theagriculture.tk/";        //"http://3.22.182.90:8000/";
    public static String userTypeURL = serverURL + "api/get-user/";
    public static String urlPost_user = serverURL + "api-token-auth/";       //rest-auth/login/serverURL + "api-token-auth/";

    public static String map_Unassigned_Admin = serverURL + "api/locations/unassigned";
    public static String map_Assigned_Admin = serverURL + "api/locations/assigned";
    public static String map_Count_Admin = serverURL + "api/count-reports/";
    public static String url_Location_Admin = serverURL + "api/upload/locations/";
    public static String url_Bulk_Admin = serverURL + "api/upload/mail/";

    public static String pendingList = serverURL + "api/locationsDatewise/pending";
    public static String smsPending = serverURL + "api/trigger/sms/pending";
    public static String ongoingList = serverURL + "api/locationsDatewise/ongoing";
    public static String completedList = serverURL + "api/locationsDatewise/completed";



    public static String User = serverURL + "api/user/";
    public static String report_ado = serverURL + "api/report-ado/";

    public static String districtUrl = serverURL + "api/district/";
    public static String usersListADO = serverURL + "api/users-list/ado/?search=";
    public static String admin = serverURL + "api/admin/";

    public static String usersList = serverURL + "api/users-list/dda/";







}
