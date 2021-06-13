/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package garment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Kawai
 */
public class BrandParser {
    ArrayList list;

    private void createContent(Node node, String indent){
         switch(node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                Document doc = (Document)node;
                createContent(doc.getDocumentElement(),"");
                break;

            case Node.ELEMENT_NODE:
                if(node.getNodeName().equals("Brand")){
                    NodeList children = node.getChildNodes();

                    if(children != null) {
                        Brand newBrand = new Brand();
                        for(int j = 0 ; j < children.getLength() ; j++){
                            createBrandContent(children.item(j),newBrand);
                            if(!(newBrand.getSerialNo().equals("") ||
                                    newBrand.getName().equals("") ||
                                    newBrand.getPrice().equals(""))){
                                list.add(newBrand);
                                break; // If no break, same brand is added twice. why?
                            }
                        }
                    }
                }
                NodeList children = node.getChildNodes();
                if(children != null){
                   for(int j = 0 ; j < children.getLength(); j++){
                        createContent(children.item(j), indent+ " ");
                    }
                }
                break;
        }
    }

    private void createBrandContent(Node current, Brand newBrand){
        Node obj;
        if(current.getNodeName().equals("SerialNo")){
            obj = current.getFirstChild();
            newBrand.setSerialNo(obj.getNodeValue());
        }
        else if(current.getNodeName().equals("BrandName")){
            obj = current.getFirstChild();
            newBrand.setName(obj.getNodeValue());
        }
        else if(current.getNodeName().equals("Price")){
            obj = current.getFirstChild();
            newBrand.setPrice(obj.getNodeValue());
        }
    }

    public BrandParser() {
        list = new ArrayList();
    }

    public List<Brand> parseFile(File f){
        DocumentBuilderFactory DBfactory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder DBbuilder = DBfactory.newDocumentBuilder();
            doc = DBbuilder.parse(f);
        }
        catch(Exception ex){
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
        createContent(doc,"");

        return list;
    }
}
