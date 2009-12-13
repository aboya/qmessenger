/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;






import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author Администратор
 */
public class dbConnection {
   // Connection conn = null;
   // CallableStatement cStmt;
     Statement  cStmt;
     Connection conn = null;
     String url = "jdbc:mysql://127.0.0.1:3306/qMessenger";
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
    public void Connect() throws Exception
    {
        conn = DriverManager.getConnection (url, userName, password);
        cStmt = conn.createStatement();
    }
    public ResultSet ExecuteQuery(String cmd) throws Exception
    {
        return cStmt.executeQuery(cmd);
    }
    public Object ExecuteScalar(String cmd)throws Exception
    {
        ResultSet rs = cStmt.executeQuery(cmd);
        if( rs.next() ) {
            return rs.getObject(1);
        }
        return null;
    }
    public void ExecuteNonQuery(String cmd) throws Exception
    {
        cStmt.execute(cmd);
    }
    private static DataSource getJdbcConnectionPool() throws NamingException {
        Context c = new InitialContext();
        return (DataSource) c.lookup("jdbc:mysql://127.0.0.1:3306/qMessenger");
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
