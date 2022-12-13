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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import net.tars.xml.tracking.Trackings;

/**
 *
 * @author Kawai
 */
@WebServlet(name="loginTracking", urlPatterns={"/loginTracking"})
public class loginTracking extends HttpServlet {
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
        String contact = request.getParameter("contact");
        String order   = request.getParameter("orderNum");
        if(contact == null || order == null) {
               request.getRequestDispatcher("loginFailure.html").forward(request, response);
        }
        else{
            doLoginTracking(contact,Integer.valueOf(order).intValue(),request,response);
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

    private void doLoginTracking(String contact, int orderNum,
            HttpServletRequest request, HttpServletResponse response)
    throws ServletException {
        EntityManager em = null;

        try {
            em = emf.createEntityManager();

            Query query = em.createQuery("SELECT p FROM PostOrder p WHERE p.orderNum = :orderNum and p.customerContact = :contact");
            query.setParameter("orderNum", orderNum);
            query.setParameter("contact", contact);
            PostOrder info = (PostOrder)query.getSingleResult();

            String xmlout = info.getTracking();

            Unmarshaller u = JAXBContext.newInstance(Trackings.class).createUnmarshaller();
            Trackings trackings = (Trackings)u.unmarshal(new StringReader(xmlout));
            List<Trackings.Tracking> records = trackings.getTracking();

            request.setAttribute("trackingList",records);

            //Forward to the jsp page for rendering
            request.getRequestDispatcher("listTracking.jsp").forward(request, response);
        } catch (NoResultException ex) {
            try {
                request.getRequestDispatcher("loginFailure.html").forward(request, response);
            }
            catch (Exception e) {
                throw new ServletException(e);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }

    }
}
