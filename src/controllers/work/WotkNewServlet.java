package controllers.work;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WotkShowServlet
 */
@WebServlet("/wotk/new")
public class WotkNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WotkNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("work_id");
        String work_date = request.getParameter("work_date");
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd"); //形式
        try {
            Date formatDate = date.parse(work_date);//DATAに変換
            String str = new SimpleDateFormat("yyyy-MM-dd").format(formatDate);//文字列に変換して時刻を消す
            request.setAttribute("work_id", id);
            request.setAttribute("work_date", str);

        } catch (ParseException e) {

        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/work/new.jsp");
        rd.forward(request, response);

    }

}
