package lbcy.com.cn.wristband.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.jph.takephoto.app.TakePhoto;

import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.utils.PhotoHelper;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 从底部滑上来的popup
 */
public class SlideFromBottomPopup extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    private PhotoHelper photoHelper;
    private TakePhoto takePhoto;

    public SlideFromBottomPopup(Activity context, TakePhoto takePhoto) {
        super(context);
        photoHelper = new PhotoHelper();
        this.takePhoto = takePhoto;
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_slide_from_bottom, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            popupView.findViewById(R.id.btnPickByTake).setOnClickListener(this);
            popupView.findViewById(R.id.btnPickBySelect).setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        photoHelper.onClick(v,takePhoto);

    }
}
