package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 运动数据明细表
 */
@Entity
public class SportDetails {
    @Id
    private Long id;//id

    private Integer steps;//步数

    private float calories;//卡路里

    private float distances;//距离

    private String createTime;//创建时间

    private String did;//设备id

    private String dateTime;//日期

    private Integer hour;//小时

    private Integer minute;//分钟

    private String startTime;//开始时间,2016-05-16 12:45

    private String endTime;//结束时间,2016-05-16 12:45

    private Integer type;//1步行，2慢跑，3跑步

    private Integer grpMinute;//分组分钟

    private String weekDate;//星期

    @Generated(hash = 50561216)
    public SportDetails(Long id, Integer steps, float calories, float distances,
            String createTime, String did, String dateTime, Integer hour,
            Integer minute, String startTime, String endTime, Integer type,
            Integer grpMinute, String weekDate) {
        this.id = id;
        this.steps = steps;
        this.calories = calories;
        this.distances = distances;
        this.createTime = createTime;
        this.did = did;
        this.dateTime = dateTime;
        this.hour = hour;
        this.minute = minute;
        this.startTime = startTime;
        this.endTime = endTime;
        this.type = type;
        this.grpMinute = grpMinute;
        this.weekDate = weekDate;
    }

    @Generated(hash = 784752352)
    public SportDetails() {
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

    public float getCalories() {
        return this.calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getDistances() {
        return this.distances;
    }

    public void setDistances(float distances) {
        this.distances = distances;
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

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getHour() {
        return this.hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return this.minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGrpMinute() {
        return this.grpMinute;
    }

    public void setGrpMinute(Integer grpMinute) {
        this.grpMinute = grpMinute;
    }

    public String getWeekDate() {
        return this.weekDate;
    }

    public void setWeekDate(String weekDate) {
        this.weekDate = weekDate;
    }



}
