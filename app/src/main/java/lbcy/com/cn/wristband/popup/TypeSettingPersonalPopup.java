package lbcy.com.cn.wristband.popup;

import android.app.Activity;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;

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
public class TypeSettingPersonalPopup extends BasePopupWindow implements View.OnClickListener{

    private View popupView;

    public TypeSettingPersonalPopup(Activity context) {
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
        popupView= LayoutInflater.from(getContext()).inflate(R.layout.popup_type_setting_personal,null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView!=null){
            popupView.findViewById(R.id.btn_cancel).setOnClickListener(this);
            popupView.findViewById(R.id.btn_submit).setOnClickListener(this);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel:
                disappearAnimation();
                break;
            case R.id.btn_submit:
                EditText editText = (EditText) findViewById(R.id.et_clock);
                saveData(editText.getText().toString());
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

}
