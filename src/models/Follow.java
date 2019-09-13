package models;

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

@Table(name = "Follow")
@NamedQueries({
        @NamedQuery(name = "getAllFollow", query = "SELECT r FROM Follow AS r ORDER BY r.id DESC"),
        @NamedQuery(name = "getFollowCount", query = "SELECT COUNT(r) FROM Follow AS r"),

        //ログイン中IDをfollowerで検索　かつ　レポート製作者IDをfolloweeで検索　一致するデータがあったら出力
        @NamedQuery(name = "getfollowid", query = "SELECT (a) FROM Follow a WHERE a.followee = :followee AND a.follower = :follower"),

        //ログインユーザだけ出力
        @NamedQuery(name = "getMyAllFollow", query = "SELECT b FROM Follow b WHERE b.follower = :follower"),

})

@Entity
public class Follow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follower", nullable = false)
    private Employee follower;

    @ManyToOne
    @JoinColumn(name = "followee", nullable = false)
    private Employee followee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFollower() {
        return follower;
    }

    public void setFollower(Employee follower) {
        this.follower = follower;
    }

    public Employee getFollowee() {
        return followee;
    }

    public void setFollowee(Employee followee) {
        this.followee = followee;
    }
}
