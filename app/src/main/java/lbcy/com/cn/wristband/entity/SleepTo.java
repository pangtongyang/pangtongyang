package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/10/24.
 */

public class SleepTo {

    /**
     * date : 2017-8-10
     * start_time : 2017-08-10 22:47:00
     * end_time : 2017-08-11 07:40:00
     * deep_duration : 200
     * light_duration : 240
     * wake_duration : 30
     * score : 85
     * history : [{"time":"2017-08-10 22:00:00","index":6},{"time":"2017-08-10 23:00:00","index":8}]
     */

    private String date;
    private String start_time;
    private String end_time;
    private int deep_duration;
    private int light_duration;
    private int wake_duration;
    private int score;
    private List<HistoryBean> history;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getDeep_duration() {
        return deep_duration;
    }

    public void setDeep_duration(int deep_duration) {
        this.deep_duration = deep_duration;
    }

    public int getLight_duration() {
        return light_duration;
    }

    public void setLight_duration(int light_duration) {
        this.light_duration = light_duration;
    }

    public int getWake_duration() {
        return wake_duration;
    }

    public void setWake_duration(int wake_duration) {
        this.wake_duration = wake_duration;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<HistoryBean> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryBean> history) {
        this.history = history;
    }

    public static class HistoryBean {
        /**
         * time : 2017-08-10 22:00:00
         * index : 6
         */

        private String time;
        private int index;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
