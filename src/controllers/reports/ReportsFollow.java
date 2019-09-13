package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsFollow
 */
@WebServlet("/reports/follow")
public class ReportsFollow extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsFollow() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Report followee_id = em.find(Report.class, Integer.parseInt(request.getParameter("report.id")));//レポートID取得
        Employee folower_id = (Employee) request.getSession().getAttribute("login_employee");//ログインID取得


        Follow f = new Follow();

        f.setFollowee(followee_id.getEmployee());
        f.setFollower(folower_id);

        //データベース更新
        em.persist(f);
        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();

        //リダイレクト
        response.sendRedirect(request.getContextPath() + "/reports/show?id="
                + Integer.parseInt(request.getParameter("report.id")));

    }
}