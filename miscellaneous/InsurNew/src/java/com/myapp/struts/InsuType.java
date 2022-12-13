/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.myapp.struts;

import java.util.List;

/**
 *
 * @author Kawai
 */
public class InsuType {
    private int    insuTypeID;
    private String insuTypeName;
    private List<InsuProduct> insuProduct;

    public InsuType() {
        insuTypeID = 0;
        insuTypeName=null;
    }

    // getter
    public int    getInsuTypeID()   { return insuTypeID; }
    public String getInsuTypeName() {return insuTypeName;}

    // setter
    public void setInsuTypeID(int _id)       {this.insuTypeID = _id; }
    public void setInsuTypeName(String _name){this.insuTypeName=_name;}
}
