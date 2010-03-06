/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

/**
 *
 * @author Администратор
 */
public class RegistrationData {
        Integer structureId = 0;
        String firstName = "";
        String lastName = "";
        String phone = "";
        String mobile = "";
        public RegistrationData()
        {
        }
        @Override
        public String toString()
        {
            return String.format("%d, '%s', '%s', '%s', '%s'", structureId, firstName, lastName, phone, mobile);

        }
        
        public static RegistrationData Parse(String s)
        {
            RegistrationData res = new RegistrationData();
            String [] inp = s.split(FormatCharacters.marker + "");
            res.structureId = Integer.valueOf(inp[0]);
            res.firstName = inp[1];
            res.lastName = inp[2];
            res.phone = inp[3];
            res.mobile = inp[4];
            return res;
        }
}
