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
    Connection conn = null;
    CallableStatement cStmt;



    public dbConnection() {
        String url = "jdbc:mysql://127.0.0.1:3306/qMessenger";
        String userName = "root";
        String password = "admin";
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection (url, userName, password);
            /*
            cStmt = conn.prepareCall("{call GetAll}");
            boolean hadResults = cStmt.execute();
            
            while(hadResults)
            {
                ResultSet rs = cStmt.getResultSet();
                rs.next();
                Object v = rs.getObject("test1");
                hadResults = cStmt.getMoreResults();
            }
             * */

          //  PreparedStatement ps = connection.prepareStatement();
         }
        catch (Exception ex) {
            Log.WriteException(ex);
        }
    }
    public ResultSet ExecuteQuery(String cmd) throws Exception
    {
        cStmt = conn.prepareCall(cmd);
        if( cStmt.execute() ) return cStmt.getResultSet();
        return null;
    }
    public Object ExecuteScalar(String cmd)throws Exception
    {
        cStmt = conn.prepareCall(cmd);
        if( cStmt.execute() ) {
            ResultSet rs = cStmt.getResultSet();
            rs.getObject(0);
        }
        return null;
    }
    public void ExecuteNonQuery(String cmd) throws Exception
    {
        cStmt = conn.prepareCall(cmd);
        cStmt.execute();
    }
    private static DataSource getJdbcConnectionPool() throws NamingException {
        Context c = new InitialContext();
        return (DataSource) c.lookup("jdbc:mysql://127.0.0.1:3306/qMessenger");
    }
    /**
     *
     * @author Администратор
     * if Id fond in db than cheks if node in db equals current node
     *      if not equal - than error
     *      else very good and continue workin
     * else add new pair in db with current node and id
     */
    public static String CheckFacultyNode(dbConnection connection, int id, String node) throws Exception
    {
        String nname =(String) connection.ExecuteScalar(
            String.format("select AddNewNode(%d,%s)", id, node)
                );
        if(nname != null) return "this id is used for " +  nname;
        return null;
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
