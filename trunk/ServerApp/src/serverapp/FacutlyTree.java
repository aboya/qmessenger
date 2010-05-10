/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
        connection = new dbConnection();
        this.ReloadTree();
    }
    public void ReloadTree()
    {
        
        isCorrect = true;
        ids.clear();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            Global.XmlTree = readFileAsString(xmlFileName);
            connection.Connect();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(xmlFileName, this);
            
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        connection.Close();
        ids.clear();
    }

    public void startElement(String uri, String localName, String qName,
		Attributes attributes) throws SAXException
    {
        
    }

    @Override
    public void startElement (String name, AttributeList attrs) throws SAXException
    {
        Integer val;
        String fname;
        val = Integer.parseInt((String)attrs.getValue("id"));
        String attrName = "";
        if(attrs.getValue("fullName") != null) attrName = attrs.getValue("fullName");
        else attrName = name;
        if(val == null) {
            isCorrect = false;
            throw new SAXException("can not find attribute ID for tag:" + name);
        }
        fname = ids.get(val);
        if( fname != null ) {
            isCorrect = false;
            throw new SAXException("Found Same ids:" + fname + " and " + name);
        }
        ids.put(val, attrName);
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
    private static String readFileAsString(String filePath) throws java.io.IOException{
        int len = (int)new File(filePath).length();
        char [] buf2 = new char[len];
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),Global.codePage));
        in.read(buf2);
        System.out.print("Xml File Read: тест\n");
        for(int i = 0; i < len; i++)
            System.out.print(buf2[i]);
        return new String(buf2).trim();
    }

 
    /**
     * @return the xml
     */
}
