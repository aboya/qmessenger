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
        String middleName = "";
        String phone = "";
        String mobile = "";
        String building = "";
        String apartaments = "";
        public RegistrationData()
        {
        }
        
        @Override
        public String toString()
        {
            return String.format("%d, '%s', '%s', '%s', '%s', '%s', '%s', '%s'",
                    structureId, firstName, lastName, phone, mobile, apartaments, building, middleName);

        }
        
        public static RegistrationData Parse(String s)
        {
            RegistrationData res = new RegistrationData();
            String [] inp = s.split(FormatCharacters.marker + "");
            res.structureId = Integer.valueOf(inp[0]);
            res.firstName = inp[1];
            res.middleName = inp[2];
            res.lastName = inp[3];
            res.phone = inp[4];
            res.mobile = inp[5];
            res.building = inp[6];
            res.apartaments = inp[7];

            return res;
        }
}
