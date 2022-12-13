/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aptech.tars.servlet;

import java.io.IOException;
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

/**
 *
 * @author Kawai
 */
@WebServlet(name="deleteOrder", urlPatterns={"/deleteOrder"})
public class deleteOrder extends HttpServlet {
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
       EntityManager em = null;
       HttpSession sn = request.getSession();
        try {
            em = emf.createEntityManager();
            Query query =em.createQuery("DELETE FROM PostOrder p WHERE p.orderNum = :orderNum");
            query.setParameter("orderNum",((Integer)sn.getAttribute("orderNum")).intValue());
//          em.getTransaction().begin(); we can not use getTransaction in JTA
            utx.begin();
            query.executeUpdate();
//          em.getTransaction().commit();
            utx.commit();
        }
        catch(Exception ex){
            throw new ServletException(ex);
        } finally {

            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }
        // clear cache. this is important.
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

}
