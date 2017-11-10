package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/4.
 */

@Entity
public class BasicBodyData {

    /**
     * height : 175
     * weight : 75
     */

    @Id
    private Long id;
    private double height;
    private double weight;
    @Generated(hash = 2037781647)
    public BasicBodyData(Long id, double height, double weight) {
        this.id = id;
        this.height = height;
        this.weight = weight;
    }
    @Generated(hash = 483214836)
    public BasicBodyData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public double getHeight() {
        return this.height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public double getWeight() {
        return this.weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

}
