package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 运动目标
 */
@Entity
public class SportTarget {
    @Id
    private Long id;//id

    private Integer steps;//步数

    @Generated(hash = 49130379)
    public SportTarget(Long id, Integer steps) {
        this.id = id;
        this.steps = steps;
    }

    @Generated(hash = 875399045)
    public SportTarget() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSteps() {
        return this.steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

}
