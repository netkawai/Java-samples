/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package garment;


import java.io.Serializable;

/**
 *
 * @author Kawai
 */
public class Brand implements Serializable {

    private String serialNo;
    private String name;
    private String price;
    public Brand() {
        serialNo = "";
        name = "";
        price = "";
    }

    public String getSerialNo() { return serialNo; }
    public String getName()     { return name; }
    public String getPrice()    { return price; }

    public void setSerialNo(String _serialNo) { serialNo = _serialNo; }
    public void setName(String _name) { name = _name; }
    public void setPrice(String _price) {price = _price; }
}
