package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsCount
 */
@WebServlet("/reports/count")
public class ReportsCount extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ReportsCount() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //CSRF対策
        request.setAttribute("_token", request.getSession().getId());
        String _token = (String) request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            //IDを取得
            Report c = em.find(Report.class, Integer.parseInt(request.getParameter("report.id")));
            //countに+1する
            Integer count = c.getCount();
            count++;
            c.setCount(count);

            //データベース更新
            em.getTransaction().begin();
            em.getTransaction().commit();
            em.close();

            response.sendRedirect(request.getContextPath() + "/reports/show?id="
                    + Integer.parseInt(request.getParameter("report.id")));


        }
    }
}