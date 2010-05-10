/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
/**
 *
 * @author Администратор
 */
public class BuildTreePath {
    private static DocumentBuilderFactory docBuilderFactory;
    private static DocumentBuilder docBuilder;
    private static Document xmlDocument;
    private static dbConnection connection = new dbConnection();

    public static  void BuildTreePath()
    {
        CreateDocument();
        NodeList nodeList = xmlDocument.getChildNodes();
        // clear previos tree in db
        try {
            connection.Connect();
            connection.ExecuteNonQuery("delete from tree_edges");

        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        connection.Close();
        
        doDfs(xmlDocument.getFirstChild());
    }
    private static void doDfs(Node node)
    {
        NodeList childNodes = node.getChildNodes();
        //if(node.getAttributes().getNamedItem("id") == null) return;
        int parent = Integer.valueOf(node.getAttributes().getNamedItem("id").getNodeValue());
        for(int i = 0, n = childNodes.getLength(); i < n; i++)
        {
            Node childNode = childNodes.item(i);
            if(childNode.getNodeName().equals("#text")) continue;
            int child = Integer.valueOf(childNode.getAttributes().getNamedItem("id").getNodeValue());
            dbFunc.AddTreeEdge(connection, parent, child);
            doDfs(childNode);
        }

    }
    private static String getStringFromXml()
    {
        try{
             StringWriter stw = new StringWriter();
             Transformer serializer = TransformerFactory.newInstance().newTransformer();
             serializer.transform(new DOMSource(xmlDocument), new StreamResult(stw));
             return stw.toString();
        }catch(Exception e)
        {
            Log.WriteException(e);
            return "";
        }

    }
    private static void CreateDocument()
    {
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        try{
            docBuilder = docBuilderFactory.newDocumentBuilder();
            xmlDocument = docBuilder.newDocument();
            xmlDocument = docBuilder.parse(new InputSource(new StringReader(Global.XmlTree)));
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
}
