/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.myapp.struts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.sql.DataSource;

/**
 *
 * @author Kawai
 */
public class InsuADO {
    DataSource dataSource;

    // constructor

    public InsuADO(Object _source) {
        dataSource = (DataSource)_source;
    }

    public ArrayList getInsuProductInfo(Integer insuTypeID) {
        ArrayList objAry = new ArrayList();
        Connection con   = null;
        PreparedStatement state = null;
        ResultSet rs= null;
        try {
            // establish the connection for derby database
            con   = dataSource.getConnection();
            state = con.prepareStatement("select * from APP.INSUPRODUCT where INSUTYPEID=?");
            state.setInt(1, insuTypeID);
            rs= state.executeQuery();
            // put object of InsuType into ArrayList.
            while(rs.next()){
                InsuProduct objInsuProduct =new InsuProduct();
                objInsuProduct.setInsuProductID(rs.getInt("INSUPRODUCTID"));
                objInsuProduct.setInsuProductName(rs.getString("INSUPRODUCTNAME"));
                objInsuProduct.setDetais(rs.getString("DETAILS"));
                objAry.add(objInsuProduct);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null)   {rs.close();}
                if(state!=null){state.close();}
                if(con!=null)   {con.close();}
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return objAry;
    }
    // A static method retrive table date to object Array.
    public ArrayList getInsuTypeInfo() {
        ArrayList objAry=new ArrayList();
        Connection db=null;
        PreparedStatement objPs=null;
        ResultSet rs=null;
        try {
            // establish the connection for derby database
            db=dataSource.getConnection();
            objPs=db.prepareStatement("SELECT * FROM INSUTYPE");
            rs=objPs.executeQuery();
            // put object of InsuType into ArrayList.
            while(rs.next()){
                InsuType objInsuType =new InsuType();
                objInsuType.setInsuTypeID(rs.getInt("INSUTYPEID"));
                objInsuType.setInsuTypeName(rs.getString("INSUTYPENAME"));
                objAry.add(objInsuType);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(rs!=null)   {rs.close();}
                if(objPs!=null){objPs.close();}
                if(db!=null)   {db.close();}
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return objAry;
    }
}
