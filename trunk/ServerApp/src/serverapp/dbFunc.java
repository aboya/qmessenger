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
    public static boolean RegisterUser(dbConnection connection,String ip,RegistrationData rData)
    {
        try {
            connection.Connect();
            connection.ExecuteNonQuery(String.format("Call AddUser('%s',%s)", ip, rData.toString()));
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
    public static UserInfo getUserInfo(dbConnection connection, String ip) throws Exception
    {
        UserInfo uInfo = null;
        connection.Connect();
        ResultSet res = connection.ExecuteQuery(
                                   String.format("call getUserInfo('%s')", ip)
                                   );
        
        if(res.next())
        {
            uInfo = UserInfo.Parse(res);
        }
        try{
            res.close();
        }catch(Exception e) {}
        try{
          connection.Close();
        }catch(Exception e) {}
        res = null;
        return uInfo;
    }
    public static void sendOfflineMessageToUser(dbConnection connection, String message, int UserID, int FromUserID) throws Exception
    {
        
        connection.Connect();
        connection.ExecuteNonQuery(
                String.format("call SendMessageToUser(%d,'%s', %d)", UserID, message, FromUserID)
                );
        connection.Close();
    }
    public static Vector <Pair> getMessagesForUser(dbConnection connection, String ip)
    {
        Vector <Pair> results = new Vector<Pair>();   
        try {
            connection.Connect();
            ResultSet res = connection.ExecuteQuery(
                       String.format("call GetMessagesForUser('%s') ",ip)
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
                    String.format("call SendFile('%s', '%s', %d, %d, '%s')",filePath, fileName, fromTreeId, whereIds[i], checkSum.toString())
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
        try {
             connection.Connect();
             rs =  connection.ExecuteQuery(String.format("call GetFilesFor('%s')", userIp));
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        //connection.Close();
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
        return rs;
    }
      //RemoveFileByID удаляет фалй из базы и возвращает количество юзеров ожидающих этот файл
    public static int RemoveFileByID(dbConnection connection, int id)
    {
        Long count = 0L;
        try {
            connection.Connect();
            count = (Long) connection.ExecuteScalar(String.format("call RemoveFileByID(%d)", id));
            if(count == null) count = 0L;
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        connection.Close();
        return count == 0 ? 0 : 1;
    }
    public static Vector<UserInfo> getUsersByTreeID(dbConnection connection,int treeId)
    {
        Vector < UserInfo > users = new Vector<UserInfo>();
        try {
            connection.Connect();
            ResultSet rs = connection.ExecuteQuery(String.format("call getUsersByTreeId(%d)", treeId));
            while(rs.next())
            {
                users.add(UserInfo.Parse(rs));
            }

        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        connection.Close();
        return users;
    }
}
