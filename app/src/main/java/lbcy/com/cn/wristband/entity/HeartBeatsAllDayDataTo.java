package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/11/2.
 */

public class HeartBeatsAllDayDataTo {

    /**
     * date : 2017-01-01
     * min_heartbeats : 71
     * max_heartbeats : 156
     * history : [{"time":"2017-08-10 08:00:00","heartbeats":100}]
     */

    private String date;
    private int min_heartbeats;
    private int max_heartbeats;
    private List<HistoryBean> history;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMin_heartbeats() {
        return min_heartbeats;
    }

    public void setMin_heartbeats(int min_heartbeats) {
        this.min_heartbeats = min_heartbeats;
    }

    public int getMax_heartbeats() {
        return max_heartbeats;
    }

    public void setMax_heartbeats(int max_heartbeats) {
        this.max_heartbeats = max_heartbeats;
    }

    public List<HistoryBean> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryBean> history) {
        this.history = history;
    }

    public static class HistoryBean {
        /**
         * time : 2017-08-10 08:00:00
         * heartbeats : 100
         */

        private String time;
        private int heartbeats;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getHeartbeats() {
            return heartbeats;
        }

        public void setHeartbeats(int heartbeats) {
            this.heartbeats = heartbeats;
        }
    }
}
