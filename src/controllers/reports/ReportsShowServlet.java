package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        Report report = em.find(Report.class, Integer.parseInt(request.getParameter("id")));//レポートの情報取得
        Employee folower_id = (Employee) request.getSession().getAttribute("login_employee");//ログイン中のユーザー名取得

        Follow follow = null;
        try {
            follow = em.createNamedQuery("getfollowid", Follow.class)//DB検索
                    .setParameter("followee", report.getEmployee())
                    .setParameter("follower", folower_id)
                    .getSingleResult();
        } catch (NoResultException e) {

        }

        em.close();


        request.setAttribute("follow", follow);
        request.setAttribute("report", report);
        request.setAttribute("_token", request.getSession().getId());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }

}

