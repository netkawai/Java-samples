/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package aptech.tars.servlet;

import aptech.tars.entities.Employee;
import java.io.IOException;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author Kawai
 */
@WebServlet(name="loginEmployee", urlPatterns={"/loginEmployee"})
public class loginEmployee extends HttpServlet {
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

        String userName =     request.getParameter("name");
        String userPassword = request.getParameter("password");
        if(userName == null || userPassword == null) {
               request.getRequestDispatcher("loginFailure.html").forward(request, response);
        }
        else{
            if((userName.compareTo("admin") == 0) && (userPassword.compareTo("admin") == 0)) {
                loginAdmin(userName,userPassword, request,response);
            }
            else {
                loginEmployee(userName,userPassword,request,response);
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

    private void saveUserInSession(String _name,HttpServletRequest request)
    {
        HttpSession sn = request.getSession();
        sn.setAttribute("name", _name);
    }

    private void loginAdmin(String _name,String _password,
            HttpServletRequest request, HttpServletResponse response)
    throws ServletException
    {
        saveUserInSession(_name,request);
        //Forward to the jsp page for rendering
        try {
            response.sendRedirect("indexAdmin.jsp");
        }
        catch(Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void loginEmployee(String _name,String _password,
            HttpServletRequest request, HttpServletResponse response)
    throws ServletException
    {
        EntityManager em = null;

        try {
            em = emf.createEntityManager();

            Query query = em.createQuery("SELECT e FROM Employee e WHERE e.name = :name and e.password = :password");
            query.setParameter("name", _name);
            query.setParameter("password", _password);
            // check user and password
            // If there is not user and password, the function throws NoResultException
            query.getSingleResult();
            saveUserInSession(_name,request);

            //Forward to the jsp page for rendering
            response.sendRedirect("indexEmployee.jsp");
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
