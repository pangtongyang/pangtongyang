package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/24.
 */

@Entity
public class SleepAllData {

    /**
     * yes_date : 2017-8-10 昨日数据
     * tod_date : 今日数据
     * start_time : 2017-08-10 22:47:00
     * end_time : 2017-08-11 07:40:00
     * deep_duration : 200
     * light_duration : 240
     * wake_duration : 30
     * score : 85
     * sleep_data : 睡眠数据
     * is_upload : 是否上传
     */

    @Id
    private Long id;
    private String yes_date;
    private String tod_date;
    private String start_time;
    private String end_time;
    private int deep_duration;
    private int light_duration;
    private int wake_duration;
    private int score;
    private String sleep_data;
    private boolean is_upload = false;
    @Generated(hash = 1177418502)
    public SleepAllData(Long id, String yes_date, String tod_date,
            String start_time, String end_time, int deep_duration,
            int light_duration, int wake_duration, int score, String sleep_data,
            boolean is_upload) {
        this.id = id;
        this.yes_date = yes_date;
        this.tod_date = tod_date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.deep_duration = deep_duration;
        this.light_duration = light_duration;
        this.wake_duration = wake_duration;
        this.score = score;
        this.sleep_data = sleep_data;
        this.is_upload = is_upload;
    }
    @Generated(hash = 1095701652)
    public SleepAllData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getYes_date() {
        return this.yes_date;
    }
    public void setYes_date(String yes_date) {
        this.yes_date = yes_date;
    }
    public String getTod_date() {
        return this.tod_date;
    }
    public void setTod_date(String tod_date) {
        this.tod_date = tod_date;
    }
    public String getStart_time() {
        return this.start_time;
    }
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
    public String getEnd_time() {
        return this.end_time;
    }
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public int getDeep_duration() {
        return this.deep_duration;
    }
    public void setDeep_duration(int deep_duration) {
        this.deep_duration = deep_duration;
    }
    public int getLight_duration() {
        return this.light_duration;
    }
    public void setLight_duration(int light_duration) {
        this.light_duration = light_duration;
    }
    public int getWake_duration() {
        return this.wake_duration;
    }
    public void setWake_duration(int wake_duration) {
        this.wake_duration = wake_duration;
    }
    public int getScore() {
        return this.score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getSleep_data() {
        return this.sleep_data;
    }
    public void setSleep_data(String sleep_data) {
        this.sleep_data = sleep_data;
    }
    public boolean getIs_upload() {
        return this.is_upload;
    }
    public void setIs_upload(boolean is_upload) {
        this.is_upload = is_upload;
    }

}
