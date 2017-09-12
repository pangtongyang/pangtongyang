package lbcy.com.cn.purplelibrary.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 睡眠数据
 */
@Entity
public class SleepInfo {
    //        select i.start_time,i.end_time,i.wake_count,i.deep_time,i.light_time,i.draw_count,i.draw_text from t_sleep_info i where i.date_time='2016-05-19'
    @Id
    private Long id;//id

    private Integer isSleepValid;//该段睡眠是否,默认0

    private Integer sleepHour;//入睡时间

    private Integer sleepMin;//入睡分钟

    private Integer wakeHour;//醒来小时

    private Integer wakeMin;//醒来分钟

    private Integer wakeCount;//醒来次数

    private Integer deepTime;//深睡眠时间，分钟

    private Integer lightTime;//浅睡眠时间，分钟

    private Integer sleepScore;//睡眠评分

    private String createTime;//创建时间

    private String did;//设备id

    private String dateTime;//日期

    private Integer drawCount;//绘图次数

    private String drawText;//1是深睡，2是醒着，3是浅睡

    private String startTime;

    private String endTime;
    //是否上传至服务器,1上传过
    private Integer isUpload;
    @Generated(hash = 874255174)
    public SleepInfo(Long id, Integer isSleepValid, Integer sleepHour, Integer sleepMin, Integer wakeHour, Integer wakeMin, Integer wakeCount, Integer deepTime,
            Integer lightTime, Integer sleepScore, String createTime, String did, String dateTime, Integer drawCount, String drawText, String startTime,
            String endTime, Integer isUpload) {
        this.id = id;
        this.isSleepValid = isSleepValid;
        this.sleepHour = sleepHour;
        this.sleepMin = sleepMin;
        this.wakeHour = wakeHour;
        this.wakeMin = wakeMin;
        this.wakeCount = wakeCount;
        this.deepTime = deepTime;
        this.lightTime = lightTime;
        this.sleepScore = sleepScore;
        this.createTime = createTime;
        this.did = did;
        this.dateTime = dateTime;
        this.drawCount = drawCount;
        this.drawText = drawText;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isUpload = isUpload;
    }
    @Generated(hash = 2000577924)
    public SleepInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getIsSleepValid() {
        return this.isSleepValid;
    }
    public void setIsSleepValid(Integer isSleepValid) {
        this.isSleepValid = isSleepValid;
    }
    public Integer getSleepHour() {
        return this.sleepHour;
    }
    public void setSleepHour(Integer sleepHour) {
        this.sleepHour = sleepHour;
    }
    public Integer getSleepMin() {
        return this.sleepMin;
    }
    public void setSleepMin(Integer sleepMin) {
        this.sleepMin = sleepMin;
    }
    public Integer getWakeHour() {
        return this.wakeHour;
    }
    public void setWakeHour(Integer wakeHour) {
        this.wakeHour = wakeHour;
    }
    public Integer getWakeMin() {
        return this.wakeMin;
    }
    public void setWakeMin(Integer wakeMin) {
        this.wakeMin = wakeMin;
    }
    public Integer getWakeCount() {
        return this.wakeCount;
    }
    public void setWakeCount(Integer wakeCount) {
        this.wakeCount = wakeCount;
    }
    public Integer getDeepTime() {
        return this.deepTime;
    }
    public void setDeepTime(Integer deepTime) {
        this.deepTime = deepTime;
    }
    public Integer getLightTime() {
        return this.lightTime;
    }
    public void setLightTime(Integer lightTime) {
        this.lightTime = lightTime;
    }
    public Integer getSleepScore() {
        return this.sleepScore;
    }
    public void setSleepScore(Integer sleepScore) {
        this.sleepScore = sleepScore;
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
    public Integer getDrawCount() {
        return this.drawCount;
    }
    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
    }
    public String getDrawText() {
        return this.drawText;
    }
    public void setDrawText(String drawText) {
        this.drawText = drawText;
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
    public Integer getIsUpload() {
        return this.isUpload;
    }
    public void setIsUpload(Integer isUpload) {
        this.isUpload = isUpload;
    }


}
