package lbcy.com.cn.wristband.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by chenjie on 2017/10/4.
 */

@Entity
public class SportStatisticsData {
    /**
     * average_steps : 5000
     * average_calorie : 1000
     * optimal_week : 8月第1周
     * optimal_date : 2017-09-10
     * total_days : 100
     * qualified_days : 80
     * goal_steps : 8000
     */

    @Id
    private Long id;
    private int average_steps;
    private double average_calorie;
    private String optimal_week;
    private String optimal_date;
    private int total_days;
    private int qualified_days;
    private int goal_steps;
    @Generated(hash = 37955180)
    public SportStatisticsData(Long id, int average_steps, double average_calorie,
            String optimal_week, String optimal_date, int total_days,
            int qualified_days, int goal_steps) {
        this.id = id;
        this.average_steps = average_steps;
        this.average_calorie = average_calorie;
        this.optimal_week = optimal_week;
        this.optimal_date = optimal_date;
        this.total_days = total_days;
        this.qualified_days = qualified_days;
        this.goal_steps = goal_steps;
    }
    @Generated(hash = 1344430914)
    public SportStatisticsData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getAverage_steps() {
        return this.average_steps;
    }
    public void setAverage_steps(int average_steps) {
        this.average_steps = average_steps;
    }
    public double getAverage_calorie() {
        return this.average_calorie;
    }
    public void setAverage_calorie(double average_calorie) {
        this.average_calorie = average_calorie;
    }
    public String getOptimal_week() {
        return this.optimal_week;
    }
    public void setOptimal_week(String optimal_week) {
        this.optimal_week = optimal_week;
    }
    public String getOptimal_date() {
        return this.optimal_date;
    }
    public void setOptimal_date(String optimal_date) {
        this.optimal_date = optimal_date;
    }
    public int getTotal_days() {
        return this.total_days;
    }
    public void setTotal_days(int total_days) {
        this.total_days = total_days;
    }
    public int getQualified_days() {
        return this.qualified_days;
    }
    public void setQualified_days(int qualified_days) {
        this.qualified_days = qualified_days;
    }
    public int getGoal_steps() {
        return this.goal_steps;
    }
    public void setGoal_steps(int goal_steps) {
        this.goal_steps = goal_steps;
    }

}
