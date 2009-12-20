/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

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
}
