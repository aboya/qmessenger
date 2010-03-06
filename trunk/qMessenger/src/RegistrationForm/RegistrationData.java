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
        String phone = "";
        String mobile = "";
        public RegistrationData()
        {
        }
        @Override
        public String toString()
        {
            return structureId.toString() + FormatCharacters.marker +
                    firstName + FormatCharacters.marker +
                    lastName + FormatCharacters.marker +
                    phone + FormatCharacters.marker +
                    mobile + FormatCharacters.marker;
        }
}
