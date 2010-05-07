/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Comunication;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Администратор
 */
public class TextMessage extends ComunicationBase {
    public Set<Integer> ids;
    public String message;
    public Boolean saveOnServer;
}
