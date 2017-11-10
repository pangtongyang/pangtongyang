package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/10/31.
 */

public class SportInfoCurrentBean {

    /**
     * goal_steps : 10000
     * done_steps : 900
     * history : [{"time":"2017-08-10 08:00:00","steps":110}]
     */

    private int goal_steps;
    private int done_steps;
    private List<HistoryBean> history;

    public int getGoal_steps() {
        return goal_steps;
    }

    public void setGoal_steps(int goal_steps) {
        this.goal_steps = goal_steps;
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
         * time : 2017-08-10 08:00:00
         * steps : 110
         */

        private String time;
        private int steps;

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
    }
}
