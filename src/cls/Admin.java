package src.cls;

public class Admin {
    private static String Admin_name;
    private static int roleid;

    public static void setUsername(String user_name){
        Admin_name=user_name;
    }
    public static String getUsername(){
        return Admin_name;
    }
    public static void setRoleID(int role_id){
        roleid=role_id;
    }
    public static int getRoleID(){
        return roleid;
    }

}
