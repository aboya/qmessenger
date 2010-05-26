/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;


import java.io.StringReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
/**
 *
 * @author Администратор
 */
public class GenerateUserTree {
    private static DocumentBuilderFactory docBuilderFactory;
    private static DocumentBuilder docBuilder;
    private static Document xmlDocument;


    public static  String getUserTree()
    {
        CreateDocument();
        NodeList nodeList = xmlDocument.getChildNodes();
        Element e;
        LinkedList<Node> Q = new LinkedList<Node>();
        int i,n = nodeList.getLength();
        for(i = 0; i < n; i++)
        {
            Q.add(nodeList.item(i));
        }
        while(!Q.isEmpty())
        {
            Node v = Q.getFirst();
            Q.removeFirst();
            if(v.getNodeName().equals("#text")) continue;

            nodeList = v.getChildNodes();
            n = nodeList.getLength();
            for(i = 0; i < n; i++)
            {
                if(!nodeList.item(i).getNodeName().equals("#text")) {
                    Q.addLast(nodeList.item(i));
                }
            }
            
            Element element = (Element)v;
            Object id = element.getAttribute("id");
            if(id == null) continue;
            int treeId = Integer.valueOf(id.toString());
            Vector<UserInfo> uInfo = dbFunc.getUsersByTreeID(
                            Global.getConnection(), treeId);
            for(UserInfo user : uInfo)
            {
                v.appendChild(CreateElement(user));
            }
        }
        return getStringFromXml();
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
            //xmlDocument = docBuilder.newDocument();
            xmlDocument = docBuilder.parse(new InputSource(new StringReader(Global.XmlTree)));
        }catch(Exception e)
        {
            Log.WriteException(e);
        }

    }
    private static Element CreateElement(UserInfo usr)
    {
        Element e = xmlDocument.createElement(usr.lastName);
        e.setAttribute("id", String.valueOf((usr.userId)));
        e.setAttribute("fullName", String.format("%s %s", usr.firstName, usr.lastName));
        e.setAttribute("isUser", "true");
        return e;
    }

}
