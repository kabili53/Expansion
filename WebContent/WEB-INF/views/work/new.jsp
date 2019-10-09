<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
  <c:param name="content">
          <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>

        <h2>出退勤　登録ページ</h2>

         <form method="POST" action="<c:url value='/work/create' />">
            <input type= "hidden" name= "work_id" value= '${work_id}' />
            <label for="date">日付</label><br />
                <input type="date" name="date_work" value="${work_date}"  required>
            <br /><br />

            <label for="name">氏名</label><br />
                  <c:out value="${sessionScope.login_employee.name}" />
            <br /><br />

            <label for="in">出勤時間</label><br />
               <input type="time" name="in_work" required>
            <br /><br />

            <label for="out">退勤時間</label><br />
                <input type="time" name="out_work" required>
            <br /><br />

            <button type="submit">登録する</button>
         </form>

    </c:param>
  </c:import>

