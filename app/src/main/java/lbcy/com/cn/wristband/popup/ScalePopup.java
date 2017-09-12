package lbcy.com.cn.wristband.popup;

import android.app.Activity;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import lbcy.com.cn.purplelibrary.utils.SPUtil;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class ScalePopup extends BasePopupWindow implements View.OnClickListener{

    private View popupView;
    private SPUtil spUtil;
    private boolean []week = new boolean[7];
    private ImageView []images = new ImageView[7];
    String ClockTAG;

    public ScalePopup(Activity context, String ClockTAG) {
        super(context);
        this.ClockTAG = ClockTAG;
        spUtil = new SPUtil(BaseApplication.getBaseApplication(), Consts.CLOCK_DK_NAME);
        bindEvent();
        loadData();
    }

    private void loadData(){
        String data = spUtil.getString(ClockTAG+"week", "");
        if (data.equals("")){
            for (int i=0; i<7; i++){
                week[i] = false;
            }
        } else {
            String []w = data.split(",");
            for (int i=0; i<7; i++){
                week[i] = w[i].equals("1");
                if (week[i]){
                    images[i].setVisibility(View.VISIBLE);
                }
            }
        }
    }



    @Override
    protected Animation initShowAnimation() {
        return getDefaultScaleAnimation();
    }


    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView= LayoutInflater.from(getContext()).inflate(R.layout.popup_normal,null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView!=null){
            popupView.findViewById(R.id.rl_zhouyi).setOnClickListener(this);
            popupView.findViewById(R.id.rl_zhouer).setOnClickListener(this);
            popupView.findViewById(R.id.rl_zhousan).setOnClickListener(this);
            popupView.findViewById(R.id.rl_zhousi).setOnClickListener(this);
            popupView.findViewById(R.id.rl_zhouwu).setOnClickListener(this);
            popupView.findViewById(R.id.rl_zhouliu).setOnClickListener(this);
            popupView.findViewById(R.id.rl_zhouri).setOnClickListener(this);
            popupView.findViewById(R.id.btn_cancel).setOnClickListener(this);
            popupView.findViewById(R.id.btn_confirm).setOnClickListener(this);
            images[0] = popupView.findViewById(R.id.iv_zhouyi);
            images[1] = popupView.findViewById(R.id.iv_zhouer);
            images[2] = popupView.findViewById(R.id.iv_zhousan);
            images[3] = popupView.findViewById(R.id.iv_zhousi);
            images[4] = popupView.findViewById(R.id.iv_zhouwu);
            images[5] = popupView.findViewById(R.id.iv_zhouliu);
            images[6] = popupView.findViewById(R.id.iv_zhouri);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_zhouyi:
                View zhouyi = findViewById(R.id.iv_zhouyi);
                if (zhouyi.getVisibility() == View.GONE){
                    week[0] = true;
                    findViewById(R.id.iv_zhouyi).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.iv_zhouyi).setVisibility(View.GONE);
                }
                break;
            case R.id.rl_zhouer:
                View zhouer = findViewById(R.id.iv_zhouer);
                if (zhouer.getVisibility() == View.GONE){
                    findViewById(R.id.iv_zhouer).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.iv_zhouer).setVisibility(View.GONE);
                }
                break;
            case R.id.rl_zhousan:
                View zhousan = findViewById(R.id.iv_zhousan);
                if (zhousan.getVisibility() == View.GONE){
                    findViewById(R.id.iv_zhousan).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.iv_zhousan).setVisibility(View.GONE);
                }
                break;
            case R.id.rl_zhousi:
                View zhousi = findViewById(R.id.iv_zhousi);
                if (zhousi.getVisibility() == View.GONE){
                    findViewById(R.id.iv_zhousi).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.iv_zhousi).setVisibility(View.GONE);
                }
                break;
            case R.id.rl_zhouwu:
                View zhouwu = findViewById(R.id.iv_zhouwu);
                if (zhouwu.getVisibility() == View.GONE){
                    findViewById(R.id.iv_zhouwu).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.iv_zhouwu).setVisibility(View.GONE);
                }
                break;
            case R.id.rl_zhouliu:
                View zhouliu = findViewById(R.id.iv_zhouliu);
                if (zhouliu.getVisibility() == View.GONE){
                    findViewById(R.id.iv_zhouliu).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.iv_zhouliu).setVisibility(View.GONE);
                }
                break;
            case R.id.rl_zhouri:
                View zhouri = findViewById(R.id.iv_zhouri);
                if (zhouri.getVisibility() == View.GONE){
                    findViewById(R.id.iv_zhouri).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.iv_zhouri).setVisibility(View.GONE);
                }
                break;
            case R.id.btn_cancel:
                disappearAnimation();
                break;
            case R.id.btn_confirm:
                saveData();
                disappearAnimation();
                break;
            default:
                break;
        }

    }

    private void disappearAnimation(){
        AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(300);

        popupView.startAnimation(disappearAnimation);
        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void saveData(){
        week[0] = findViewById(R.id.iv_zhouyi).getVisibility() == View.VISIBLE;
        week[1] = findViewById(R.id.iv_zhouer).getVisibility() == View.VISIBLE;
        week[2] = findViewById(R.id.iv_zhousan).getVisibility() == View.VISIBLE;
        week[3] = findViewById(R.id.iv_zhousi).getVisibility() == View.VISIBLE;
        week[4] = findViewById(R.id.iv_zhouwu).getVisibility() == View.VISIBLE;
        week[5] = findViewById(R.id.iv_zhouliu).getVisibility() == View.VISIBLE;
        week[6] = findViewById(R.id.iv_zhouri).getVisibility() == View.VISIBLE;
        String mWeek = (week[0]?"1":"0") + "," + (week[1]?"1":"0") + "," + (week[2]?"1":"0") + ","
                + (week[3]?"1":"0") + "," + (week[4]?"1":"0") + "," + (week[5]?"1":"0") + "," + (week[6]?"1":"0");
        int allFlag = 0;
        String text = "";
        for (int i=0; i<7; i++){
            if (week[i]){
                allFlag++;
                switch(i){
                    case 0:
                        text = "周一";
                        break;
                    case 1:
                        if (text.equals("")){
                            text = "周二";
                        } else {
                            text += "，周二";
                        }
                        break;
                    case 2:
                        if (text.equals("")){
                            text = "周三";
                        } else {
                            text += "，周三";
                        }
                        break;
                    case 3:
                        if (text.equals("")){
                            text = "周四";
                        } else {
                            text += "，周四";
                        }
                        break;
                    case 4:
                        if (text.equals("")){
                            text = "周五";
                        } else {
                            text += "，周五";
                        }
                        break;
                    case 5:
                        if (text.equals("")){
                            text = "周六";
                        } else {
                            text += "，周六";
                        }
                        break;
                    case 6:
                        if (text.equals("")){
                            text = "周日";
                        } else {
                            text += "，周日";
                        }
                        break;
                }
            }
        }
        if (allFlag == 0) {
            text = "只响一次";
        }
        if (allFlag == 7) {
            text = "每日";
        }

        spUtil.putString(ClockTAG+"week", mWeek);
        spUtil.putString(ClockTAG+"weekText", text);

        Message message = new Message();
        message.what = Consts.UPDATE_CLOCK_DATA;
        message.obj = text;
        RxBus.getInstance().post(Consts.CLOCK_LISTENER, message);
    }
}
