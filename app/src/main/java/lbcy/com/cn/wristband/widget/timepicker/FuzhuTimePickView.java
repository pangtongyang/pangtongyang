package lbcy.com.cn.wristband.widget.timepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lbcy.com.cn.wristband.R;

/**
 * Created by admin on 17/3/13.
 */

public class FuzhuTimePickView extends FrameLayout {
	LoopView loopDay;
	LoopView loopHour;
	LoopView loopMin;

	public FuzhuTimePickView(Context context) {
		super(context);
		//View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_time, this);
		LayoutInflater.from(context).inflate(R.layout.layout_fuzhu_picker_time, this, true);
		loopDay = (LoopView) findViewById(R.id.loop_day);
		loopHour = (LoopView) findViewById(R.id.loop_hour);

		List<String> days = new ArrayList<>();
		days.add("起床");
		loopDay.setCyclic(false);
		loopDay.setArrayList(days);
		loopDay.setCurrentItem(0);

		//修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
		loopHour.setCyclic(false);
		loopHour.setArrayList(d(0, 24));
		loopHour.setCurrentItem(12);

		loopMin = (LoopView) findViewById(R.id.loop_min);
		loopMin.setCyclic(false);
		loopMin.setArrayList(d(0, 60));
		loopMin.setCurrentItem(30);

	}

	public FuzhuTimePickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.layout_fuzhu_picker_time, this, true);
		loopDay = (LoopView) findViewById(R.id.loop_day);
		loopHour = (LoopView) findViewById(R.id.loop_hour);
		loopMin = (LoopView) findViewById(R.id.loop_min);

		//修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
		List<String> days = new ArrayList<>();
		days.add("起床");
		loopDay.setCyclic(false);
		loopDay.setArrayList(days);
		loopDay.setCurrentItem(0);

		loopHour.setCyclic(false);
		loopHour.setArrayList(d(0, 24));
		loopHour.setCurrentItem(12);

		loopMin.setCyclic(false);
		loopMin.setArrayList(d(0, 60));
		loopMin.setCurrentItem(30);
	}

	public int[] getSelectTime(){
		int[] time = new int[2];
		final LoopView loopHour = (LoopView) findViewById(R.id.loop_hour);
		final LoopView loopMin = (LoopView) findViewById(R.id.loop_min);
		loopHour.getCurrentItem();
		time[0] = loopHour.getCurrentItem();
		time[1] = loopMin.getCurrentItem();
		return time;
	}


	/**
	 * 将数字传化为集合，并且补充0
	 *
	 * @param startNum 数字起点
	 * @param count    数字个数
	 * @return
	 */
	private static List<String> d(int startNum, int count) {
		String[] values = new String[count];
		for (int i = startNum; i < startNum + count; i++) {
			String tempValue = (i < 10 ? "0" : "") + i;
			values[i - startNum] = tempValue;
		}
		return Arrays.asList(values);
	}

	public void setTime(int hour, int minute){
		loopHour.setCurrentItem(hour);
		loopMin.setCurrentItem(minute);
	}
}
