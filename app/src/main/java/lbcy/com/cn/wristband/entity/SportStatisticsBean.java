package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/10/4.
 */

public class SportStatisticsBean {

    /**
     * code : 200
     * message : null
     * data : {"average_steps":5000,"average_calorie":1000,"optimal_week":"8月第1周","optimal_date":"2017-09-10","total_days":100,"qualified_days":80,"goal_steps":8000}
     */

    private int code;
    private Object message;
    private SportStatisticsData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public SportStatisticsData getData() {
        return data;
    }

    public void setData(SportStatisticsData data) {
        this.data = data;
    }

}
