/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.sql.ResultSet;

/**
 *
 * @author Администратор
 */
public class UserInfo {
    public String firstName = "";
    public String lastName = "";
    public int treeId = 0;
    public String treeName = "";
    public String ip = "";
    public int userId = -1;

    public static UserInfo Parse(ResultSet rs)
    {
        UserInfo uInfo = new UserInfo();
        try{

           uInfo.firstName = rs.getString("FirstName");
           uInfo.lastName = rs.getString("LastName");
           uInfo.treeId = rs.getInt("TreeID");
           uInfo.treeName = rs.getString("TreeName");
           uInfo.userId = rs.getInt("UserID");
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        return uInfo;
        
    }

    @Override
    public String toString() {
        return firstName + FormatCharacters.marker +
                lastName + FormatCharacters.marker +
                treeName + FormatCharacters.marker +
                String.valueOf(treeId)+ FormatCharacters.marker +
                String.valueOf(userId);
    }


}
