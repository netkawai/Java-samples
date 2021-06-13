
package jaxbsample1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


public class MarshallerSample1 {
    public MarshallerSample1() {
        try {
            // 1. JAXBContext�I�u�W�F�N�g�̐���
            // ��̓p�b�P�[�W�������̓N���X
            JAXBContext context 
                = JAXBContext.newInstance("net.javainthebox.xml");

            // 2. Marsaller�I�u�W�F�N�g�̎擾
            Marshaller marshaller = context.createMarshaller();

            // 3. �}�[�V�������O����I�u�W�F�N�g������
            Persons persons = createPersons();

            // 4. �}�[�V�������O�o�͐�
            //    �o�͂ɂ̓X�g���[�����g�p
            FileOutputStream stream 
                = new FileOutputStream("artists2.xml");
 
            // 5. �}�[�V�������O
            marshaller.marshal(persons, stream);
        } catch (FileNotFoundException ex) {
            // ��O����
            System.err.println("artists2.xml ���I�[�v���ł��܂���");
        } catch (JAXBException ex) {
            // ��O����
            System.err.println("�}�[�V�������O�Ɏ��s���܂���");
        }
    }

    private Persons createPersons() {
        Persons persons = new Persons();
 
        Person person = new Person();
        person.setName("Paul McCartney");
        person.setAge(66);
        person.setSex(Sex.MALE);
        persons.getPerson().add(person);
         
        person = new Person();
        person.setName("Mick Jagger");
        person.setAge(65);
        person.setSex(Sex.MALE);
        persons.getPerson().add(person);
        
        person = new Person();
        person.setName("Mary Hopkins");
        person.setAge(58);
        person.setSex(Sex.FEMALE);
        persons.getPerson().add(person);
        
        return persons;
    }

    public static void main(String[] args) {
        new MarshallerSample1();
    }
}
