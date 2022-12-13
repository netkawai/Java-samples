/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.myapp.struts;

/**
 *
 * @author Kawai
 */
public class InsuProduct {
    private int    insuProductID;
    private String insuProductName;
    private String details;

    public InsuProduct(){
        insuProductID = 0;
        insuProductName = null;
        details = null;
    }

        // getter
    public int    getInsuProductID()   { return insuProductID; }
    public String getInsuProductName() {return insuProductName;}
    public String getDetails() {return details; }
    // setter
    public void setInsuProductID(int _id)       {this.insuProductID = _id; }
    public void setInsuProductName(String _name){this.insuProductName=_name;}
    public void setDetais(String _details) {this.details = _details; }
}
