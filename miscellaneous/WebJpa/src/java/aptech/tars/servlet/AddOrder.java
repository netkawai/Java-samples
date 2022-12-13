/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aptech.tars.servlet;

import aptech.tars.entities.Employee;
import aptech.tars.entities.PostCost;
import aptech.tars.entities.PostOrder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.GregorianCalendar;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import net.tars.xml.tracking.Trackings;

/**
 *
 * @author Kawai
 */
@WebServlet(name="AddOrder", urlPatterns={"/AddOrder"})
public class AddOrder extends HttpServlet {
    @PersistenceUnit
    private EntityManagerFactory emf;
    @Resource
    UserTransaction utx;
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        HttpSession sn = request.getSession();


        PostOrder newOrder = new PostOrder();

        newOrder.setCustomerContact((String)sn.getAttribute("contact"));
        newOrder.setSrcAddr((String)sn.getAttribute("from"));
        newOrder.setDstAddr((String)sn.getAttribute("to"));
        newOrder.setQuantity((Integer)sn.getAttribute("quantity"));

        Employee employee = getEmployee(sn);
        newOrder.setEmployee(employee);

        newOrder.setPostCost(getPostCost(sn));

        OutputStream xmlout = null;

        // create initial tracking
        net.tars.xml.tracking.ObjectFactory objFactory = new net.tars.xml.tracking.ObjectFactory();
        net.tars.xml.tracking.Trackings.Tracking tracking = objFactory.createTrackingsTracking();
        tracking.setAddress((String)sn.getAttribute("from"));
        tracking.setReceiver(employee.getName());
        tracking.setDate(getSystemTime());

        // add tracking to trackins
        net.tars.xml.tracking.Trackings trackings = objFactory.createTrackings();
        trackings.getTracking().add(tracking);
        try
        {
        Marshaller ms = JAXBContext.newInstance(Trackings.class).createMarshaller();
        xmlout = new ByteArrayOutputStream();
        ms.setProperty( Marshaller.JAXB_FRAGMENT, Boolean.TRUE );
        ms.marshal(trackings,xmlout);
        }
        catch(Exception ex){
            throw new ServletException(ex);
        }
        newOrder.setTracking(xmlout.toString());

       EntityManager em = null;
        try {
            em = emf.createEntityManager();

//          em.getTransaction().begin(); we can not use getTransaction in JTA
            utx.begin();
            em.persist(newOrder);
//          em.getTransaction().commit();
            utx.commit();
        }
        catch(Exception ex){
            throw new ServletException(ex);
        } finally {
            sn.removeAttribute("contact");
            sn.removeAttribute("from");
            sn.removeAttribute("to");
            sn.removeAttribute("quantity");
            sn.removeAttribute("weight");
            sn.removeAttribute("service");

            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }
        // clear cache. this is important.
        emf.getCache().evictAll();

        response.sendRedirect("listOrder");
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    
    private XMLGregorianCalendar getSystemTime()
    throws ServletException
    {
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
        }
        catch(Exception ex){
            throw new ServletException(ex);
        }
    }
    private PostCost getPostCost(HttpSession sn)
    throws ServletException
    {
       EntityManager em = null;
       PostCost post = null;
       try {
            em = emf.createEntityManager();

            Query query = em.createQuery("SELECT p FROM PostCost p WHERE p.serviceType = :serviceType and p.weight = :weight");
            query.setParameter("serviceType", sn.getAttribute("service"));
            query.setParameter("weight", sn.getAttribute("weight"));

            post = (PostCost)query.getSingleResult();
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }
        return post;
    }

    private Employee getEmployee(HttpSession sn)
    throws ServletException
    {
        EntityManager em = null;
        Employee employee = null;

        try {
            em = emf.createEntityManager();
            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.name = :name");
            query.setParameter("name", sn.getAttribute("name"));
            // check user
            // If there is not user and password, the function throws NoResultException
            employee = (Employee)query.getSingleResult();
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }
        return employee;
    }
}
