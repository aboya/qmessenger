/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

import clientapp.FormatCharacters;

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
            return structureId.toString() + FormatCharacters.marker +
                    firstName + FormatCharacters.marker +
                    middleName + FormatCharacters.marker +
                    lastName + FormatCharacters.marker +
                    phone + FormatCharacters.marker +
                    mobile + FormatCharacters.marker +
                    building + FormatCharacters.marker +
                    apartaments + FormatCharacters.marker;
        }
}
