package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/10/21.
 */

public class SportHeartRateTo {

    /**
     * sport_code : 10001
     * end_time : 2017-08-10 03:04:00
     * duration : 00:30:00
     * average_heartbeats : 125
     * history : [{"time":"2017-08-10 03:00:00","heartbeats":153,"steps":100}]
     */

    private int sport_code;
    private String end_time;
    private String duration;
    private int average_heartbeats;
    private List<HistoryBean> history;

    public int getSport_code() {
        return sport_code;
    }

    public void setSport_code(int sport_code) {
        this.sport_code = sport_code;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getAverage_heartbeats() {
        return average_heartbeats;
    }

    public void setAverage_heartbeats(int average_heartbeats) {
        this.average_heartbeats = average_heartbeats;
    }

    public List<HistoryBean> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryBean> history) {
        this.history = history;
    }

    public static class HistoryBean {
        /**
         * time : 2017-08-10 03:00:00
         * heartbeats : 153
         * steps : 100
         */

        private String time;
        private int heartbeats;
        private int steps;

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

        public int getSteps() {
            return steps;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }
    }
}
