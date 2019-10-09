package controllers.work;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Work;
import utils.DBUtil;

/**
 * Servlet implementation class WorkIndexServlet
 */
@WebServlet("/work/index")
public class WorkIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorkIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee employee_id = (Employee) request.getSession().getAttribute("login_employee");//ログイン中のユーザー名取得
        int plus_count = 1;
        int minus_count = -1;

        //日付をセット
        Calendar cl = Calendar.getInstance();
        int year = cl.get(Calendar.YEAR); //年を取得
        int moon = cl.get(Calendar.MONTH); //月を取得
        cl.set(year, moon, 1);//先月＆来月の日付をセット
        int lastday = cl.getActualMaximum(Calendar.DAY_OF_MONTH);//月の最終日を取得

        //時刻を消す
        cl.clear(Calendar.MINUTE);
        cl.clear(Calendar.SECOND);
        cl.clear(Calendar.MILLISECOND);
        cl.set(Calendar.HOUR_OF_DAY, 0);

        //初日
        cl.set(year, moon, 1);
        Date day_3 = cl.getTime();
        //最終日
        cl.set(year, moon, lastday);
        Date day_2 = cl.getTime();

        //1日目
        cl.set(year, moon, 1);
        Date day_1 = cl.getTime();

        Work calendar_1 = new Work();
        calendar_1.setWork_date(day_1); //保存
        calendar_1.setEmployee(employee_id);

         //タイトル部分のカレンダー表示
        if(moon == 0){
           int minus_month = 12;    //0月だったら12月にする
           request.setAttribute("minus_month", minus_month);
        }else{
            request.setAttribute("minus_month", moon);
        }

        int plus_month =moon + 2;
        if(plus_month == 13){
            int a = 1;    //13月になったら1月にする
            request.setAttribute("plus_month", a);
        }else{
            request.setAttribute("plus_month", plus_month);
        }

        //DBに同じの日付があったら戻る
        Work login_work;
        try {
            login_work = em.createNamedQuery("getWorkEmployee", Work.class)//日付検索
                    .setParameter("employee", employee_id)
                    .setParameter("work_date", day_1)
                    .getSingleResult();

            List<Work> allwork = em.createNamedQuery("getCalendar", Work.class)//存在しなかったらリスト取得
                    .setParameter("dete1", day_3)
                    .setParameter("dete2", day_2)
                    .setParameter("employee", employee_id)
                    .getResultList();

            double sum = (double)em.createNamedQuery("getSum", Double.class)
                    .setParameter("dete1", day_1)//月初めの日から
                    .setParameter("dete2", day_2)//月終わりの日までを検索
                    .setParameter("employee", employee_id)
                    .getSingleResult();

            request.setAttribute("sum", sum);
            request.setAttribute("allwork", allwork);
            request.setAttribute("plus_count", plus_count);
            request.setAttribute("minus_count", minus_count);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/work/index.jsp");
            rd.forward(request, response);
            return;
        } catch (NoResultException e) {

        }

        em.persist(calendar_1);//存在しなかったら保存

        //2日目以降
        for (int i = 1; i < lastday; i++) {
            cl.add(Calendar.DAY_OF_MONTH, 1);
            Date date = cl.getTime();

            Work calendar_2 = new Work(); //2日目以降保存
            calendar_2.setWork_date(date);
            calendar_2.setEmployee(employee_id);
            em.persist(calendar_2);
        }

        em.getTransaction().begin();
        em.getTransaction().commit();

        //該当の日付のみ取得
        List<Work> allwork = em.createNamedQuery("getCalendar", Work.class)
                .setParameter("dete1", day_3)
                .setParameter("dete2", day_2)
                .setParameter("employee", employee_id)
                .getResultList();

        request.setAttribute("allwork", allwork);
        request.setAttribute("plus_count", plus_count);
        request.setAttribute("minus_count", minus_count);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/work/index.jsp");
        rd.forward(request, response);
    }

}
