package controllers.work;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Work;
import utils.DBUtil;

/**
 * Servlet implementation class WorkCreateServlet
 */
@WebServlet("/work/create")
public class WorkCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WorkCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        //残業時間計算
        try {
            String out_work = request.getParameter("out_work");
            String in_work = request.getParameter("in_work");
            SimpleDateFormat out = new SimpleDateFormat("HH:mm");
            SimpleDateFormat in = new SimpleDateFormat("HH:mm");

            //Dateに変換
            Date in_date = in.parse(in_work);
            Date out_date = out.parse(out_work);

            //計算するためCalendarに変換
            Calendar in_calendar = Calendar.getInstance();
            in_calendar.setTime(in_date);
            int in_hour = in_calendar.get(Calendar.HOUR_OF_DAY);
            int in_min = in_calendar.get(Calendar.MINUTE);

            Calendar out_calendar = Calendar.getInstance();//退社-（出社+就労時間8時間）
            out_calendar.setTime(out_date);

            out_calendar.add(Calendar.MINUTE, -in_min); //先に分を計算

            int out_hour = out_calendar.get(Calendar.HOUR_OF_DAY);

            if (out_hour - in_hour <= 8) { //勤務時間が8時間以下だったら0
                out_calendar.set(Calendar.HOUR_OF_DAY, 00);
                out_calendar.set(Calendar.MINUTE, 00);

            } else {
                out_calendar.add(Calendar.HOUR_OF_DAY, -(in_hour + 8));
            }

            Work new_work = em.find(Work.class, Integer.parseInt(request.getParameter("work_id")));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            new_work.setOver_clock(sdf.format(out_calendar.getTime()));

            //残業時間の合計用
            double over_hour = out_calendar.get(Calendar.HOUR_OF_DAY);
            double over_min = out_calendar.get(Calendar.MINUTE);
            double k = over_min / 60; //分を小数に直す
            double db = Double.parseDouble(String.format("%.1f", k));//小数第1位に変換
            double over_sum = over_hour + db;

            new_work.setOver_sum(over_sum);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Work new_work = em.find(Work.class, Integer.parseInt(request.getParameter("work_id")));
        new_work.setIn_clock(request.getParameter("in_work"));
        new_work.setOut_clock(request.getParameter("out_work"));

        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();
        response.sendRedirect(request.getContextPath() + "/work/index");

    }
}
