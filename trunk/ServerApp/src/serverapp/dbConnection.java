/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;






import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
/**
 *
 * @author Администратор
 */
public class dbConnection {
   // Connection conn = null;
   // CallableStatement cStmt;
     Statement  cStmt;
     Connection conn = null;
     String url = Global.dbUrl;
     String userName = "root";
     String password = "admin";

    public dbConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
         }
        catch (Exception ex) {
            Log.WriteException(ex);
        }
    }
    public static String ConvertString(String cmd) throws Exception
    {
        return cmd;
    }
    public void Connect() throws Exception
    {
        Properties properties=new Properties();
        properties.setProperty("useUnicode","true");
        properties.setProperty("characterEncoding",Global.codePage);
        properties.setProperty("user",userName);
        properties.setProperty("password",password);
        conn = DriverManager.getConnection (url, properties);
        cStmt = conn.createStatement();
    }
    public ResultSet ExecuteQuery(String cmd) throws Exception
    {
        cmd = ConvertString(cmd);
        return cStmt.executeQuery(cmd);
    }
    public Object ExecuteScalar(String cmd)throws Exception
    {
        cmd = ConvertString(cmd);
        ResultSet rs = cStmt.executeQuery(cmd);
        if( rs.next() ) {
            return rs.getObject(1);
        }
        return null;
    }
    public void ExecuteNonQuery(String cmd) throws Exception
    {
        cmd = ConvertString(cmd);
        cStmt.execute(cmd);
    }
    public void Close()
    {
        try {
            conn.close();
            conn = null;
        }catch(Exception e) {
            Log.WriteException(e);
        }
    }


}
