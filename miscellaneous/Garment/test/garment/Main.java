/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package garment;

import garment.Brand;
import garment.BrandParser;
import java.io.File;
import java.util.List;

/**
 *
 * @author Kawai
 */
public class Main {
    public static void main(String args[]) {
        File f = new File("Brandlist.xml");
        BrandParser parser = new BrandParser();
        List<Brand> list = parser.parseFile(f);
        
        for(int j = 0 ; j < list.size() ; j++){
            Brand brand = list.get(j);
            System.out.println("SerialNo" + brand.getSerialNo());
            System.out.println("Name" + brand.getName());
            System.out.println("Price" + brand.getPrice());
        }
    }
}
