package models;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "work")
@NamedQueries({
    @NamedQuery(
            name = "getAllWork", //ログイン中IDのタイムカード取得
            query = "SELECT (a) FROM Work a WHERE a.employee = :employee"
            ),
    @NamedQuery(
            name = "getworkCount",//件数を取得
            query = "SELECT COUNT(a) FROM Work a WHERE a.employee = :employee"
            ),
    @NamedQuery(
            name = "getWorkEmployee",//ログイン中IDのデータとその日付を取得
            query = "SELECT (a) FROM Work a WHERE a.employee = :employee AND a.work_date = :work_date"
            ),
    @NamedQuery(
            name = "getCalendar",//選択された月のみを表示
            query = "SELECT (a) FROM Work a WHERE a.employee = :employee AND a.work_date BETWEEN :dete1 AND :dete2 "
            ),
    @NamedQuery(
            name = "getSum",//選択された月のみを表示
            query = "SELECT SUM(a.over_sum) FROM Work a WHERE a.employee = :employee AND a.work_date BETWEEN :dete1 AND :dete2 "

            )

})

@Entity
public class Work{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee", nullable = false)
    private Employee employee;

    @Column(name = "work_date", nullable = false)
    private Date work_date;

    @Column(name = "in_clock")
    private String in_clock;

    @Column(name = "out_clock")
    private String out_clock;

    @Column(name = "over_clock")
    private String over_clock;

    @Column(name = "over_sum")
    private double over_sum;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getWork_date() {
        return work_date;
    }

    public void setWork_date(Date work_date) {
        this.work_date = work_date;
    }

    public String getIn_clock() {
        return in_clock;
    }

    public void setIn_clock(String in_clock) {
        this.in_clock = in_clock;
    }

    public String getOut_clock() {
        return out_clock;
    }

    public void setOut_clock(String out_clock) {
        this.out_clock = out_clock;
    }

    public String getOver_clock() {
        return over_clock;
    }

    public void setOver_clock(String over_clock) {
        this.over_clock = over_clock;
    }

    public double getOver_sum() {
        return over_sum;
    }

    public void setOver_sum(double over_sum) {
        this.over_sum = over_sum;
    }


}
