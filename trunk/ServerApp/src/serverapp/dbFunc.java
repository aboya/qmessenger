/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author Администратор
 */
public class dbFunc {
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
            String.format("select AddNewNode(%d,'%s')", id, node)
                );
        if(nname != null && nname.length() > 0 && !nname.equalsIgnoreCase(node)) return "this id is used for " +  nname;
        return null;
    }
    public static boolean RegisterUser(dbConnection connection,String ip, String structureId)
    {
        try {
            connection.Connect();
            connection.ExecuteNonQuery(String.format("Call AddUser('%s',%s)", ip, structureId));
        }catch(Exception e)
        {
            Log.WriteException(e);
            return false;
        }
        connection.Close();
        return true;
    }
    public static ResultSet getUserTreeName(dbConnection connection, String ip, String treeName, Integer TreeID) throws Exception
    {
        
        connection.Connect();
        ResultSet res = connection.ExecuteQuery(
                                   String.format("call getUserTreeName('%s')", ip)
                                   );

        if(res.next())
        {
            return res;
        }
        res.close();
        connection.Close();
        return null;
    }
    public static void sendOfflineMessageToUser(dbConnection connection, String message, int TreeID, int FromTreeID) throws Exception
    {
        
        connection.Connect();
        connection.ExecuteNonQuery(
                String.format("call SendMessageToUser(%d,'%s', %d)", TreeID, message, FromTreeID)
                );
        connection.Close();
    }
    public static Vector <Pair> getMessagesForUser(dbConnection connection, Integer TreeID, String ip)
    {
        Vector <Pair> results = new Vector<Pair>();   
        try {
            connection.Connect();
            ResultSet res = connection.ExecuteQuery(
                       String.format("call GetMessagesForUser('%s', %d) ",ip, TreeID)
                    );
            while(res.next())
            {
                Pair p = new Pair(res.getString("message"), res.getString("TreeName"));
                results.add(p);
            }

        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        connection.Close();
        return results;
    }
}
