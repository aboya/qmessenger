/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;



import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.*;
import javax.xml.parsers.SAXParserFactory; 
import javax.xml.parsers.SAXParser;

/**
 *
 * @author Администратор
 */
public class FacutlyTree extends HandlerBase {

    private String xmlFileName;
    private boolean isCorrect;
    dbConnection connection;
    public Map<Integer, String> ids;
    public FacutlyTree(String fileName) {
        xmlFileName = fileName;
        ids = new TreeMap<Integer, String>();
        this.ReloadTree();
    }
    public void ReloadTree()
    {
        connection = new dbConnection();
        isCorrect = true;
        ids.clear();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(xmlFileName, this);
            
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        connection.Close();
        ids.clear();
    }
    @Override
    public void startElement (String name, AttributeList attrs) throws SAXException
    {
        Integer val;
        String fname;
        val = Integer.parseInt((String)attrs.getValue("id"));
        if(val == null) {
            isCorrect = false;
            throw new SAXException("can not find attribute ID for tag:" + name);
        }
        fname = ids.get(val);
        if( fname != null ) {
            isCorrect = false;
            throw new SAXException("Found Same ids:" + fname + " and " + name);
        }
        ids.put(val, name);
        String errorMess = ""; // not null !!
        try {
            errorMess =(String) dbFunc.CheckFacultyNode(connection, val, name);
        }catch( Exception e )
        {
            Log.WriteException(e);
            isCorrect = false;
        }
        if(errorMess != null  ) throw new SAXException(errorMess);
    }

}
