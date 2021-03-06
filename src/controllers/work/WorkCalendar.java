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
 * Servlet implementation class WorkTest
 */
@WebServlet("/work/calendar")
public class WorkCalendar extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorkCalendar() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Employee employee_id = (Employee) request.getSession().getAttribute("login_employee");//ログイン中のユーザー名取得
        int date_count = Integer.parseInt(request.getParameter("date_count")); //先月か来月か

        //カレンダー前後
        int plus_count = date_count ;
        plus_count ++;
        int minus_count = date_count;
        minus_count --;

        Calendar cl = Calendar.getInstance();
        int year = cl.get(Calendar.YEAR); //年を取得
        int moon = cl.get(Calendar.MONTH); //月を取得
        cl.set(year, moon + date_count, 1);//先月＆来月の日付をセット
        int lastday = cl.getActualMaximum(Calendar.DAY_OF_MONTH);//月の最終日を取得

        //時刻を消す
        cl.clear(Calendar.MINUTE);
        cl.clear(Calendar.SECOND);
        cl.clear(Calendar.MILLISECOND);
        cl.set(Calendar.HOUR_OF_DAY, 0);

        //1日目
        cl.set(year, moon + date_count, 1);
        Date day_1 = cl.getTime();//月の初めの日

        Work calendar_1 = new Work();
        calendar_1.setWork_date(day_1);
        calendar_1.setEmployee(employee_id);

        //最終日
        cl.set(year, moon+ date_count, lastday);
        Date day_2 = cl.getTime();


        //タイトル部分のカレンダー表示
        int header_month = cl.get(Calendar.MONTH);

        if(header_month == 0){ //0月だったら12月にする
           int a = 12;
           request.setAttribute("minus_month", a);
        }else{
            request.setAttribute("minus_month", header_month);
        }

        int plus_month =header_month + 2;
        if(plus_month == 13){   //13月だったら1月にする
            int b = 1;
            request.setAttribute("plus_month", b);
        }else{
            request.setAttribute("plus_month", plus_month);
        }

        Work login_work ;
        try {
            login_work = em.createNamedQuery("getWorkEmployee", Work.class) //月の初めの日付を検索
                    .setParameter("employee", employee_id)
                    .setParameter("work_date", day_1)
                    .getSingleResult();

            //存在したらリストを取得して戻る
            List<Work> allwork = em.createNamedQuery("getCalendar", Work.class)
                    .setParameter("dete1", day_1)//月初めの日から
                    .setParameter("dete2", day_2)//月終わりの日までを検索
                    .setParameter("employee", employee_id)
                    .getResultList();

            double sum = (double)em.createNamedQuery("getSum", Double.class) //残業時間の合計を取得
                    .setParameter("dete1", day_1)//月初めの日から
                    .setParameter("dete2", day_2)//月終わりの日までを検索
                    .setParameter("employee", employee_id)
                    .getSingleResult();

            request.setAttribute("plus_count", plus_count);
            request.setAttribute("minus_count", minus_count);
            request.setAttribute("allwork", allwork);
            request.setAttribute("sum", sum);

            em.close();
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/work/index.jsp");
            rd.forward(request, response);
            return;
        } catch (NoResultException e) {

        }
        em.persist(calendar_1);//存在しなかったらDBに保存

        //2日目以降
        cl.set(year, moon + date_count, 1);

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

        List<Work> allwork = em.createNamedQuery("getCalendar", Work.class)
                .setParameter("dete1", day_1)
                .setParameter("dete2", day_2)
                .setParameter("employee", employee_id)
                .getResultList();

        request.setAttribute("allwork", allwork);
        request.setAttribute("plus_count", plus_count);
        request.setAttribute("minus_count", minus_count);

        em.close();
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/work/index.jsp");
        rd.forward(request, response);

    }

}
