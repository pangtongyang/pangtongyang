package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/10/17.
 */

public class SportTo {

    /**
     * date : 2017-08-10
     * done_steps : 1942
     * history : [{"date":"2017-08-10 08:00:00","steps":100,"duration":"00:30:15"},{"date":"2017-08-10 09:00:00","steps":110,"duration":"00:30:15"},{"date":"2017-08-10 10:00:00","steps":120,"duration":"00:30:15"}]
     */

    private String date;
    private int done_steps;
    private List<HistoryBean> history;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDone_steps() {
        return done_steps;
    }

    public void setDone_steps(int done_steps) {
        this.done_steps = done_steps;
    }

    public List<HistoryBean> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryBean> history) {
        this.history = history;
    }

    public static class HistoryBean {
        /**
         * date : 2017-08-10 08:00:00
         * steps : 100
         * duration : 00:30:15
         */

        private String time;
        private int steps;
        private String duration;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getSteps() {
            return steps;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }
}
