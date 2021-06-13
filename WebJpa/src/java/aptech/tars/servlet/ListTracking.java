/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aptech.tars.servlet;

import aptech.tars.entities.PostOrder;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import net.tars.xml.tracking.Trackings;
/**
 *
 * @author Kawai
 */
@WebServlet(name="ListTracking", urlPatterns={"/ListTracking"})
public class ListTracking extends HttpServlet {
    @PersistenceUnit
    private EntityManagerFactory emf;

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
        try {

            //query for all the persons in database
//            List persons = em.createQuery("select p from Person p").getResultList();
            HttpSession sn = request.getSession();
            sn.setAttribute("orderNum", Integer.valueOf(request.getParameter("orderNum")));

            em = emf.createEntityManager();
            Query query = em.createNamedQuery("PostOrder.findByOrderNum");
            query.setParameter("orderNum",sn.getAttribute("orderNum"));
            PostOrder info = (PostOrder)query.getSingleResult();

            String xml = info.getTracking();

            Unmarshaller u = JAXBContext.newInstance(Trackings.class).createUnmarshaller();
            Trackings trackings = (Trackings)u.unmarshal(new StringReader(xml));
            List<Trackings.Tracking> records = trackings.getTracking();

            request.setAttribute("trackingList",records);

            //Forward to the jsp page for rendering
            request.getRequestDispatcher("TrackingInfo.jsp").forward(request, response);
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }
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

}
