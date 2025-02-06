package src.cls;

import src.db.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AddUserCode {
    private static final String prefix = "STU";
    private static final int nbDigit = 6;

    private static String formatNumber(int nbDigit, int number){
        String formatString = "%0"+nbDigit+"d";
        return String.format(formatString,number);
    }
    private static String getAddUserCode(){
        Connection connection = DBConnection.getConnection();
        String code = null;
        //get latest code from db
        try{
            StringBuilder query = new StringBuilder();
            query.append("SELECT CODE")
                    .append(" FROM ")
                    .append(" ADDUSER")
                    .append(" ORDER BY ID DESC LIMIT 1");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());
            if (resultSet.next()){
                code = resultSet.getString("code");
            }
            connection.close();
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        finally {
            return code;
        }
    }
    public static String generateAddUserCode(){
        String newNumberPart="";
        try{
            String oldCode = getAddUserCode();
            if (oldCode!=null)
            {
                String numberPart = oldCode.substring(prefix.length());
                int numberValue = Integer.parseInt(numberPart);
                numberValue ++;
                newNumberPart = formatNumber(nbDigit,numberValue);
            }
            else{
                newNumberPart = formatNumber(nbDigit,1);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            return prefix.concat(newNumberPart);
        }
    }
}


