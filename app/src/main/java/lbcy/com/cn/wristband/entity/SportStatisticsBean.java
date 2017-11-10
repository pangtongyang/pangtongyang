package lbcy.com.cn.wristband.entity;

/**
 * Created by chenjie on 2017/10/4.
 */

public class SportStatisticsBean {

    /**
     * code : 200
     * message : null
     * data : {"average_steps":3683,"average_calorie":246.07,"optimal_week":"10月第5周","optimal_date":"2017-10-31","total_days":7,"qualified_days":0,"goal_steps":9999}
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
