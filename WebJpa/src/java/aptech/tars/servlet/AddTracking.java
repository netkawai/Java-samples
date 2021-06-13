/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aptech.tars.servlet;

import aptech.tars.entities.PostOrder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
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
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import net.tars.xml.tracking.Trackings;

/**
 *
 * @author Kawai
 */
@WebServlet(name="AddTracking", urlPatterns={"/AddTracking"})
public class AddTracking extends HttpServlet {
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
        assert emf != null;  //Make sure injection went through correctly.
        EntityManager em = null;
        HttpSession sn = request.getSession();
        try {
            PostOrder info = null;
            em = emf.createEntityManager();
            {
                Query query = em.createNamedQuery("PostOrder.findByOrderNum");
                query.setParameter("orderNum",sn.getAttribute("orderNum"));
                info = (PostOrder)query.getSingleResult();
            }

            String xmlin = info.getTracking();

            Unmarshaller u = JAXBContext.newInstance(Trackings.class).createUnmarshaller();
            Trackings trackings = (Trackings)u.unmarshal(new StringReader(xmlin));

            // create add new tracking
            net.tars.xml.tracking.ObjectFactory objFactory = new net.tars.xml.tracking.ObjectFactory();
            net.tars.xml.tracking.Trackings.Tracking tracking = objFactory.createTrackingsTracking();
            tracking.setAddress(request.getParameter("address"));
            tracking.setReceiver(request.getParameter("receiver"));
            tracking.setDate(getSystemTime());

            trackings.getTracking().add(tracking);

            OutputStream xmlout = null;
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


            {
                Query query = em.createQuery("UPDATE PostOrder p SET p.tracking = :tracking WHERE p.orderNum = :orderNum");
                query.setParameter("tracking", xmlout.toString());
                query.setParameter("orderNum",sn.getAttribute("orderNum"));
                utx.begin();
                query.executeUpdate();
                utx.commit();
            }

        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }
        emf.getCache().evictAll();
        sn.removeAttribute("orderNum");
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

}
