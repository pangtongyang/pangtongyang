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
    private int height;
    private int weight;
    @Generated(hash = 1783880232)
    public BasicBodyData(Long id, int height, int weight) {
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
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWeight() {
        return this.weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }

}
