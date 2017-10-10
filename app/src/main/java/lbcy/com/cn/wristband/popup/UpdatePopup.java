package lbcy.com.cn.wristband.popup;

import android.app.Activity;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Locale;

import butterknife.BindView;
import lbcy.com.cn.wristband.R;
import lbcy.com.cn.wristband.app.BaseApplication;
import lbcy.com.cn.wristband.global.Consts;
import lbcy.com.cn.wristband.rx.RxBus;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/15.
 * 普通的popup
 */
public class UpdatePopup extends BasePopupWindow implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvSpeed;
    private ProgressBar pbDoing;
    private RelativeLayout rlProgress;
    private Button btnCancel;
    private Button btnPauseCont;

    private View popupView;
    private BasePopupWindow popupWindow;
    private String title;
    private String filepath;
    private String url;

    int type = 1;
    int updateStatus = 0; // 0 -> 下载过程 1 -> 升级过程
    int downloadId;

    /**
     *
     * @param context 上下文
     * @param type 升级类型 1 -> MCU 2 -> BLUETOOTH 3 -> HARDWARE
     * @param title 标题
     * @param url 文件下载网址
     */
    public UpdatePopup(Activity context, int type, String title, String url) {
        super(context);
        bindEvent();
        this.title = title;
        this.url = url;
        this.type = type;
        filepath = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + Consts.FILE_DOWNLOAD_CACHE_DIR + File.separator +
                (type==1 ? Consts.FILE_MCU_NAME : (type==2 ? Consts.FILE_BLUETOOTH_NAME : Consts.FILE_HARDWARE_NAME));
        doing();
    }

    private void doing(){
        downloadId = createDownloadTask(type).start();
    }

    @Override
    protected Animation initShowAnimation() {
        return getDefaultScaleAnimation();
    }


    @Override
    public View getClickToDismissView() {
        return null;
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_file_download, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            tvTitle = popupView.findViewById(R.id.tv_title);
            tvSubTitle = popupView.findViewById(R.id.tv_sub_title);
            tvSpeed = popupView.findViewById(R.id.tv_speed);
            pbDoing = popupView.findViewById(R.id.pb_doing);
            rlProgress = popupView.findViewById(R.id.rl_progress);
            btnCancel = popupView.findViewById(R.id.btn_cancel);
            btnPauseCont = popupView.findViewById(R.id.btn_pause_cont);
            btnCancel.setOnClickListener(this);
            btnPauseCont.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (updateStatus == 1){
                    Toast.makeText(BaseApplication.getBaseApplication(), "升级过程中，禁止取消", Toast.LENGTH_SHORT).show();
                    return;
                }
                FileDownloader.getImpl().pause(downloadId);
                disappearAnimation();

                break;
            case R.id.btn_pause_cont:
                if (updateStatus == 1){
                    Toast.makeText(BaseApplication.getBaseApplication(), "升级过程中，禁止暂停", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (btnPauseCont.getText().toString().equals(getContext().getString(R.string.pause))){
                    btnPauseCont.setText(getContext().getString(R.string.cont));
                    FileDownloader.getImpl().pause(downloadId);
                } else {
                    btnPauseCont.setText(getContext().getString(R.string.pause));
                    downloadId = createDownloadTask(type).start();
                }

                break;

            default:
                break;
        }

    }

    private void disappearAnimation() {
        AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(300);

        popupView.startAnimation(disappearAnimation);
        disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new File(filepath).delete();
                new File(FileDownloadUtils.getTempPath(filepath)).delete();
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    // type 1 -> MCU 2 -> BLUETOOTH 3 -> HARDWARE
    private BaseDownloadTask createDownloadTask(int type){
        final ViewHolder tag;
        final String url;
        boolean isDir = false;
        String path;

        url = this.url;
        tag = new ViewHolder(new WeakReference<>(this), pbDoing, tvSpeed);
        path = filepath;

        return FileDownloader.getImpl().create(url)
                .setPath(path, isDir)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setTag(tag)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        ((ViewHolder) task.getTag()).updatePending(task);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        ((ViewHolder) task.getTag()).updateProgress(soFarBytes, totalBytes,
                                task.getSpeed());
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        ((ViewHolder) task.getTag()).updateCompleted(task);
                        updateStatus = 1;
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        ((ViewHolder) task.getTag()).updatePaused(task.getSpeed());
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        ((ViewHolder) task.getTag()).updateError(e, task.getSpeed());
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        ((ViewHolder) task.getTag()).updateWarn();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        ((ViewHolder) task.getTag()).updateConnected(etag, task.getFilename());
                    }
                });
    }

    private static class ViewHolder {
        private ProgressBar pb;
        private TextView speedTv;
        private WeakReference<UpdatePopup> weakReferenceContext;

        private ViewHolder(WeakReference<UpdatePopup> weakReferenceContext, final ProgressBar pb, final TextView speedTv){
            this.weakReferenceContext = weakReferenceContext;
            this.pb = pb;
            this.speedTv = speedTv;
        }

        private void updateSpeed(int speed){
            speedTv.setText(String.format(Locale.getDefault(), "%dKB/s", speed));
        }

        private void updateProgress(int sofar, int total, int speed){
            if (total == -1){
                pb.setIndeterminate(true);
            } else {
                pb.setMax(total);
                pb.setProgress(sofar);
            }

            updateSpeed(speed);
        }

        private void updatePending(BaseDownloadTask task){

        }

        private void updatePaused(int speed) {
            updateSpeed(speed);
        }

        private void updateConnected(String etag, String filename) {

        }

        private void updateWarn() {
            toast("下载警告！");
            pb.setIndeterminate(false);
        }

        private void updateError(Throwable ex, int speed) {
            toast(String.format("下载出错 %s", ex));
            updateSpeed(speed);
            pb.setIndeterminate(false);
            ex.printStackTrace();
        }

        private void updateCompleted(BaseDownloadTask task) {

            toast("下载完成！");

            updateSpeed(task.getSpeed());
            pb.setIndeterminate(false);
            pb.setMax(task.getSmallFileTotalBytes());
            pb.setProgress(task.getSmallFileSoFarBytes());
        }

        private void toast(String msg){
            Toast.makeText(BaseApplication.getBaseApplication(), msg, Toast.LENGTH_SHORT).show();

        }
    }

}
