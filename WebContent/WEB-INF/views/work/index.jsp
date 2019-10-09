<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>勤怠管理システム&nbsp;&nbsp;
        </h2>

        <form style="display:inline" method="POST" action="<c:url value='/work/calendar' />">
             <input type="hidden" name="date_count" value="${minus_count}" />
            ${minus_month}月＜＜＜ &nbsp;<button type="submit">先月</button>&nbsp;
        </form>

        <form style="display:inline" method="POST" action="<c:url value='/work/calendar' />">
              <input type="hidden" name="date_count" value="${0}" />
              <button type="submit">今月</button>&nbsp;
        </form>

        <form style="display:inline" method="POST" action="<c:url value='/work/calendar' />">
             <input type="hidden" name="date_count" value="${plus_count}" />
             <button type="submit">来月</button>&nbsp;＞＞＞${plus_month}月
        </form>


    <p align="right">残業時間の合計は<span style="font-size:30px;">${sum}</span>hです</p>
        <c:choose>
            <c:when test="${sum >= 40}" >
                 <p align="right" class="over_work">上限を超えています</p>
            </c:when>
        </c:choose>

        <table id="work_list">
            <tbody>
                <tr>
                    <th class="work_name">氏名</th>
                    <th class="work_date">日付</th>
                    <th class="work_in">出勤時刻</th>
                    <th class="work_out">退勤時刻</th>
                    <th class="work_over">残業時間 </th>
                    <th class="work_show">詳細</th>
                </tr>
                <c:forEach var="allwork" items="${allwork}" varStatus="status">
                    <tr class="row${status.count % 2}">
                       <td class="work_name"><c:out value="${allwork.employee.name}" /></td>
                       <td class="work_date"><fmt:formatDate value= '${allwork.work_date}' pattern='yyyy-MM-dd' /> </td>
                       <td class="work_in"><c:out value= "${allwork.in_clock}"  /></td>
                       <td class="work_out"><c:out value="${allwork.out_clock}" /></td>
                       <td class="work_over"><c:out value="${allwork.over_clock}" /></td>
                       <td class="work_show">
                         <c:choose>
                            <c:when test="${allwork.in_clock == null }" >
                                 <form method="POST" action="<c:url value='/wotk/new' />">
                                    <input type= "hidden" name= "work_date" value= '${allwork.work_date}' />
                                    <input type= "hidden" name= "work_id" value= '${allwork.id}' />
                                    <button type="submit">新規登録</button>
                                 </form>
                            </c:when>
                            <c:otherwise>
                                入力済みです
                            </c:otherwise>
                          </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:param>
</c:import>
