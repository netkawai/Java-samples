/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jaxbsample1;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Kawai
 */
public class UnmarshallerSample {
    public UnmarshallerSample() {
        try {
            // 1. JAXBContextオブジェクトの生成
            // 引数はパッケージもしくはクラス
            JAXBContext context =
                JAXBContext.newInstance("jaxbsample1");

            // 2. Unmarsallerオブジェクトの取得
            Unmarshaller unmarshaller = context.createUnmarshaller();

            File file = new File("contact.xml");

            // 3. アンマーシャリング
            // 戻り値はルートタグに相当するクラス
            Object obj = unmarshaller.unmarshal(file);
            Persons artists = (Persons)obj;

            // 4. 変換されたオブジェクトにアクセス
            java.util.List<Person> p = artists.getPerson();
            for (Person person: artists.getPerson()) {
                System.out.printf("%s\tAge: %2d Sex: %s%n",
                                  person.getName(),
                                  person.getAge(),
                                  person.getSex());
            }

        } catch (JAXBException ex) {
            // 例外処理
        }
    }

    public static void main(String[] args) {
        new UnmarshallerSample();
    }
}
