/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.sql.ResultSet;
import java.util.Set;
import java.util.TreeSet;
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
    public static ResultSet getUserTreeName(dbConnection connection, String ip) throws Exception
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
    public static void SendFileToUser(dbConnection connection, String fromIp, Integer [] whereIds, String fileName, String filePath, Long checkSum)
    {
        int fromTreeId;
        ResultSet rs = null;
        try {
            rs = getUserTreeName(connection, fromIp.substring(1));
            fromTreeId = rs.getInt("TreeID");
            connection.Connect();
            fileName = EscapeCharecters(fileName);
            filePath = EscapeCharecters(filePath);
            for(int i = 0; i < whereIds.length; i++)
            {
                connection.ExecuteNonQuery(
                    String.format("call SendFile('%s', '%s', %d, %d, '%s')",fileName, filePath, fromTreeId, whereIds[i], checkSum.toString())
                        );
            }

        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        try {
           if(rs != null) rs.close();
        }catch(Exception ee) {}
    }
    public static String EscapeCharecters(String s)
    {

        String res = "";
        Set <Character> chars = new TreeSet<Character>();
        chars.add('\\');
        chars.add('\'');
        for(int i = 0; i < s.length(); i++ )
        {
            if(chars.contains(s.charAt(i)))
                res += '\\';
            res += s.charAt(i);
        }
        return res;

    }
    public static ResultSet getFilesForUser(dbConnection connection, String userIp)
    {
        ResultSet rs = null;
        int TreeID;
        try {
            rs = getUserTreeName(connection, userIp);
            connection.Connect();
            TreeID = rs.getInt("TreeID");
            rs =  connection.ExecuteQuery(String.format("call GetFilesFor(%d)", TreeID));
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        connection.Close();
        return rs;
    }
    public static ResultSet getFilePathByID(dbConnection connection, int id)
    {
        String path = null;
        ResultSet rs = null;
        try {
            connection.Connect();
            rs = connection.ExecuteQuery(String.format("call getFilePathByID(%d)", id));
            
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        try {
            connection.Close();
        }catch(Exception ee) {}
        return rs;
    }
    public static int RemoveFileByID(dbConnection connection, int id)
    {
        int count = 0;
        try {
            connection.Connect();
            count = (Integer)connection.ExecuteScalar(String.format("call RemoveFileByID(%d)", id));

        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        try {
            connection.Close();
        }catch(Exception ee) {}
        return count;

    }
}
