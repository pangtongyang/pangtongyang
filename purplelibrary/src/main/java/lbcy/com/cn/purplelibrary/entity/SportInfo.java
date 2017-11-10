package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 运动数据表
 */
@Entity
public class SportInfo {
    @Id
    private Long id;//id

    private Integer steps;//步数

    private float distances;//距离

    private float calories;//卡路里

    private String createTime;//创建时间

    private String did;//设备id

    private String battery;//电量

    private String dateTime;//日期

    //是否上传至服务器,1上传过
    private Integer isUpload;

    @Generated(hash = 486779361)
    public SportInfo(Long id, Integer steps, float distances, float calories,
            String createTime, String did, String battery, String dateTime,
            Integer isUpload) {
        this.id = id;
        this.steps = steps;
        this.distances = distances;
        this.calories = calories;
        this.createTime = createTime;
        this.did = did;
        this.battery = battery;
        this.dateTime = dateTime;
        this.isUpload = isUpload;
    }

    @Generated(hash = 1254911132)
    public SportInfo() {
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

    public float getDistances() {
        return this.distances;
    }

    public void setDistances(float distances) {
        this.distances = distances;
    }

    public float getCalories() {
        return this.calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDid() {
        return this.did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getBattery() {
        return this.battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(Integer isUpload) {
        this.isUpload = isUpload;
    }



}
