/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

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
    public int userId = 0;

    public static UserInfo Parse(String s)
    {
        UserInfo uInfo = new UserInfo();
        try{
            String [] data = s.split(FormatCharacters.marker + "");
            uInfo.firstName = data[0];
            uInfo.lastName = data[1];
            uInfo.treeName = data[2];
            uInfo.treeId = Integer.valueOf(data[3]);
            uInfo.userId = Integer.valueOf(data[4]);

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
                String.valueOf(treeId);
    }


}
