package lbcy.com.cn.wristband.popup;

import android.app.Activity;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class TypeSettingPopup extends BasePopupWindow implements View.OnClickListener{

    private View popupView;
    private BasePopupWindow popupWindow;

    public TypeSettingPopup(Activity context) {
        super(context);
        bindEvent();
        loadData();
    }

    private void loadData(){

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
        popupView= LayoutInflater.from(getContext()).inflate(R.layout.popup_type_setting,null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView!=null){
            popupView.findViewById(R.id.rl_type_1).setOnClickListener(this);
            popupView.findViewById(R.id.rl_type_2).setOnClickListener(this);
            popupView.findViewById(R.id.rl_type_3).setOnClickListener(this);
            popupView.findViewById(R.id.rl_type_4).setOnClickListener(this);
            popupView.findViewById(R.id.rl_type_5).setOnClickListener(this);
            popupView.findViewById(R.id.rl_type_6).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_type_1:
                TextView tvType1 = (TextView) findViewById(R.id.tv_type_1);
                saveData(tvType1.getText().toString());
                disappearAnimation();

                break;
            case R.id.rl_type_2:
                TextView tvType2 = (TextView) findViewById(R.id.tv_type_2);
                saveData(tvType2.getText().toString());
                disappearAnimation();
                break;
            case R.id.rl_type_3:
                TextView tvType3 = (TextView) findViewById(R.id.tv_type_3);
                saveData(tvType3.getText().toString());
                disappearAnimation();
                break;
            case R.id.rl_type_4:
                TextView tvType4 = (TextView) findViewById(R.id.tv_type_4);
                saveData(tvType4.getText().toString());
                disappearAnimation();
                break;
            case R.id.rl_type_5:
                TextView tvType5 = (TextView) findViewById(R.id.tv_type_5);
                saveData(tvType5.getText().toString());
                disappearAnimation();
                break;
            case R.id.rl_type_6:
                popupWindow = getPopupTypeSettingPersonal();
                popupWindow.showPopupWindow();
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

    private void saveData(String text){

        Message message = new Message();
        message.what = Consts.UPDATE_CLOCK_TYPE_DATA;
        message.obj = text;
        RxBus.getInstance().post(Consts.CLOCK_LISTENER, message);
    }

    BasePopupWindow getPopupTypeSettingPersonal() {
        return new TypeSettingPersonalPopup(getContext());
    }
}
