package lbcy.com.cn.wristband.popup;

import android.app.Activity;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.entity.ClockBean;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class SedentarySpaceTimeSettingPopup extends BasePopupWindow implements View.OnClickListener{

    private View popupView;

    public SedentarySpaceTimeSettingPopup(Activity context) {
        super(context);
        bindEvent();
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
        popupView= LayoutInflater.from(getContext()).inflate(R.layout.popup_sedentary_space_time_setting,null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView!=null){
            popupView.findViewById(R.id.rl_30).setOnClickListener(this);
            popupView.findViewById(R.id.rl_60).setOnClickListener(this);
            popupView.findViewById(R.id.rl_90).setOnClickListener(this);
            popupView.findViewById(R.id.rl_120).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_30:
                saveData(((TextView)findViewById(R.id.tv_30)).getText().toString());
                break;
            case R.id.rl_60:
                saveData(((TextView)findViewById(R.id.tv_60)).getText().toString());
                break;
            case R.id.rl_90:
                saveData(((TextView)findViewById(R.id.tv_90)).getText().toString());
                break;
            case R.id.rl_120:
                saveData(((TextView)findViewById(R.id.tv_120)).getText().toString());
                break;
            default:
                break;
        }
        disappearAnimation();
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

    private void saveData(String text){

        Message message = new Message();
        message.what = Consts.UPDATE_SEDENTARY_SPACE_TIME;
        message.obj = text;
        RxBus.getInstance().post(Consts.ACTIVITY_SEDENTARY_LISTENER, message);
    }
}
