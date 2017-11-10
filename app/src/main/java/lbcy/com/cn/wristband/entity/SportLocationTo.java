package lbcy.com.cn.wristband.entity;

import java.util.List;

/**
 * Created by chenjie on 2017/10/21.
 */

public class SportLocationTo {

    /**
     * sport_code : 10001
     * history : [{"time":"2017-09-09 00:01:00","longitude":123.4,"latitude":234.1}]
     */

    private int sport_code;
    private List<HistoryBean> history;

    public int getSport_code() {
        return sport_code;
    }

    public void setSport_code(int sport_code) {
        this.sport_code = sport_code;
    }

    public List<HistoryBean> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryBean> history) {
        this.history = history;
    }

    public static class HistoryBean {
        /**
         * time : 2017-09-09 00:01:00
         * longitude : 123.4
         * latitude : 234.1
         */

        private String time;
        private double longitude;
        private double latitude;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
