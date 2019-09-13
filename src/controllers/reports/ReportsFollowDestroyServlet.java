package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsFollowDestroyServlet
 */
@WebServlet("/follow/destroy")
public class ReportsFollowDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsFollowDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();
        Follow delete = em.find(Follow.class, Integer.parseInt(request.getParameter("follow.id")));

        em.getTransaction().begin();
        em.remove(delete); //結果を受け取って消去
        em.getTransaction().commit();
        em.close();

        response.sendRedirect(request.getContextPath() + "/reports/show?id="
                + Integer.parseInt(request.getParameter("report.id")));
    }
}