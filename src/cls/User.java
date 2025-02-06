package src.cls;

public class User {
    private static String username;
    private static int roleid;

    public static void setUsername(String user_name){
        username=user_name;
    }
    public static String getUsername(){
        return username;
    }
    public static void setRoleID(int role_id){
        roleid=role_id;
    }
    public static int getRoleID(){
        return roleid;
    }

}
