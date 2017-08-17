package f.star.iota.milk.ui.settings;

import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import f.star.iota.milk.R;
import f.star.iota.milk.SourceType;
import f.star.iota.milk.base.BaseActivity;
import f.star.iota.milk.config.OtherConfig;
import f.star.iota.milk.config.SplashConfig;

public class DisplayAndDownloadActivity extends BaseActivity {
    @BindView(R.id.bubble_seek_bar_span_count)
    BubbleSeekBar mBubbleSeekBarSpanCount;
    @BindView(R.id.bubble_seek_bar_download_count)
    BubbleSeekBar mBubbleSeekBarDownloadCount;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.radio_group_source)
    RadioGroup mRadioGroupBannerSource;
    @BindView(R.id.radio_apic)
    RadioButton mRadioButtonApic;
    @BindView(R.id.radio_moeimg)
    RadioButton mRadioButtonMoeimg;
    @BindView(R.id.radio_bing)
    RadioButton mRadioButtonBing;
    @BindView(R.id.radio_gank)
    RadioButton mRadioButtonGank;
    @BindView(R.id.radio_wallhaven)
    RadioButton mRadioButtonWallHaven;
    @BindView(R.id.radio_simple_desktops)
    RadioButton mRadioButtonSimpleDesktops;
    @BindView(R.id.radio_yuriimg)
    RadioButton mRadioButtonYuriimg;

    @Override
    protected void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mBubbleSeekBarSpanCount.setProgress(OtherConfig.getSpanCountConfig(aContext));
        mBubbleSeekBarSpanCount.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                OtherConfig.saveSpanCountConfig(mContext, progress);
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });
        mBubbleSeekBarDownloadCount.setProgress(OtherConfig.getDownloadCountConfig(aContext));
        mBubbleSeekBarDownloadCount.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int i, float v) {
                OtherConfig.saveDownloadCountConfig(mContext, i);
            }

            @Override
            public void getProgressOnActionUp(int i, float v) {

            }

            @Override
            public void getProgressOnFinally(int i, float v) {

            }
        });
        switch (SplashConfig.getSplashSource(mContext)) {
            case SourceType.APIC:
                mRadioButtonApic.setChecked(true);
                break;
            case SourceType.BING:
                mRadioButtonBing.setChecked(true);
                break;
            case SourceType.GANK:
                mRadioButtonGank.setChecked(true);
                break;
            case SourceType.MOEIMG:
                mRadioButtonMoeimg.setChecked(true);
                break;
            case SourceType.WALLHAVEN:
                mRadioButtonWallHaven.setChecked(true);
                break;
            case SourceType.SIMPLEDESKTOPS:
                mRadioButtonSimpleDesktops.setChecked(true);
                break;
            case SourceType.YURIIMG:
                mRadioButtonYuriimg.setChecked(true);
                break;
        }
        mRadioGroupBannerSource.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id) {
                    case R.id.radio_apic:
                        SplashConfig.saveSplashSource(mContext, SourceType.APIC);
                        break;
                    case R.id.radio_moeimg:
                        SplashConfig.saveSplashSource(mContext, SourceType.MOEIMG);
                        break;
                    case R.id.radio_bing:
                        SplashConfig.saveSplashSource(mContext, SourceType.BING);
                        break;
                    case R.id.radio_gank:
                        SplashConfig.saveSplashSource(mContext, SourceType.GANK);
                        break;
                    case R.id.radio_wallhaven:
                        SplashConfig.saveSplashSource(mContext, SourceType.WALLHAVEN);
                        break;
                    case R.id.radio_simple_desktops:
                        SplashConfig.saveSplashSource(mContext, SourceType.SIMPLEDESKTOPS);
                        break;
                    case R.id.radio_yuriimg:
                        SplashConfig.saveSplashSource(mContext, SourceType.YURIIMG);
                        break;
                }
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_display_download;
    }

}
