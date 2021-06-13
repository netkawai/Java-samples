/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aptech.tars.servlet;

import aptech.tars.entities.PostCost;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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

/**
 *
 * @author Kawai
 */
@WebServlet(name="calculationOrder", urlPatterns={"/calculationOrder"})
public class calculationOrder extends HttpServlet {
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

        String weight      = (String)request.getParameter("weight");
        String serviceType = (String)request.getParameter("service");
        String quantity    = (String)request.getParameter("quantity");
        
        
        if(weight == null || serviceType == null || quantity == null) {
            request.getRequestDispatcher("calculateFailure.html").forward(request, response);
        }
        else{            
            calculationPostCost(weight,serviceType ,Integer.valueOf(quantity),request,response);
        }
        request.getRequestDispatcher("calculationResult.jsp").forward(request, response);
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

    private void saveCostInSession(String weight, String serviceType, Integer quantity,HttpServletRequest request)
    {
        HttpSession sn = request.getSession();
        if(!sn.isNew() && (sn.getAttribute("name")!=null)) {
            sn.setAttribute("weight", weight);
            sn.setAttribute("service", serviceType);
            sn.setAttribute("quantity", quantity);
        }
    }

    private void calculationPostCost(String weight, String serviceType, Integer quantity,
            HttpServletRequest request, HttpServletResponse response)
    throws ServletException
    {
      EntityManager em = null;

       try {
            em = emf.createEntityManager();

            Query query = em.createQuery("SELECT p FROM PostCost p WHERE p.serviceType = :serviceType and p.weight = :weight");
            query.setParameter("serviceType", serviceType);
            query.setParameter("weight", weight);

            PostCost post = (PostCost)query.getSingleResult();
            float totalcost = post.getCost().multiply(new BigDecimal(quantity)).floatValue();
            request.setAttribute("weight", weight);
            request.setAttribute("service", serviceType);
            request.setAttribute("quantity", quantity);
            request.setAttribute("cost", totalcost);

            saveCostInSession(weight,serviceType,quantity,request);
       }
        catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            //close the em to release any resources held up by the persistebce provider
            if(em != null) {
                em.close();
            }
        }
    }
}
