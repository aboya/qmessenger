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
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
/**
 *
 * @author Администратор
 */
public class dbConnection {

    public dbConnection() {
        String url = "jdbc:mysql://127.0.0.1:3306/qMessenger";
        String userName = "root";
        String password = "admin";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = null;
            conn = DriverManager.getConnection (url, userName, password);
            CallableStatement cStmt = conn.prepareCall("{call GetAll}");
            boolean hadResults = cStmt.execute();
            
            while(hadResults)
            {
                ResultSet rs = cStmt.getResultSet();
                rs.next();
                Object v = rs.getObject("test1");
                hadResults = cStmt.getMoreResults();
            }

          //  PreparedStatement ps = connection.prepareStatement();
         }
        catch (Exception ex) {
            Log.WriteException(ex);
        }
    }
    private static DataSource getJdbcConnectionPool() throws NamingException {
        Context c = new InitialContext();
        return (DataSource) c.lookup("jdbc:mysql://127.0.0.1:3306/qMessenger");
}


}
