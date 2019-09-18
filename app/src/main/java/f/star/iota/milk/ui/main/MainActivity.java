package f.star.iota.milk.ui.main;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.task.XExecutor;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import f.star.iota.milk.Menus;
import f.star.iota.milk.Net;
import f.star.iota.milk.R;
import f.star.iota.milk.base.BaseActivity;
import f.star.iota.milk.base.BaseFragment;
import f.star.iota.milk.base.RVBean;
import f.star.iota.milk.config.OtherConfig;
import f.star.iota.milk.config.ThemeConfig;
import f.star.iota.milk.ui.download.DownloadManagerActivity;
import f.star.iota.milk.ui.menu.MenuIllustrationFragment;
import f.star.iota.milk.ui.menu.MenuMeiziFragment;
import f.star.iota.milk.ui.menu.MenuWallpaperFragment;
import f.star.iota.milk.ui.moeimg.moe.MoeimgFragment;
import f.star.iota.milk.ui.more.MoreActivity;
import f.star.iota.milk.ui.search.SearchFragment;
import f.star.iota.milk.ui.settings.SettingsActivity;
import f.star.iota.milk.util.MediaUtils;
import f.star.iota.milk.util.MessageBar;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import moe.feng.alipay.zerosdk.AlipayZeroSdk;

public class MainActivity extends BaseActivity implements XExecutor.OnAllTaskEndListener, MainActivityContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private Drawer drawer;

    private MainActivityPresenter mPresenter;

    private boolean isRunning;
    private TextView mTextViewHitokoto;
    private TextView mTextViewHitokotoSrc;
    private BaseFragment currentFragment;
    private CloseableReference<CloseableImage> mCloseableImageCloseableReference;


    @Override
    protected void init() {
        create();
        initDrawer();
        initDrawerEvent();
        isShowDonationDialog();
    }

    private void isShowDonationDialog() {
        long openCount = OtherConfig.getOpenCount(mContext);
        if ((openCount % 16 == 0 || openCount == 5) && OtherConfig.isShowDonation(mContext)) {
            showDonationDialog();
        } else if (openCount % 100 == 0) {
            MessageBar.create(mContext, "这是您打开的 " + openCount + " 次，将冒昧的显示捐赠页面");
            showDonationDialog();
        }
    }

    private void showDonationDialog() {
        mToolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                donation();
            }
        }, 3600);
    }

    private void donation() {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.dialog_donation, null);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .setNegativeButton("下次吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNeutralButton("不再提醒", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        OtherConfig.saveDonationStatus(mContext, false);
                        dialogInterface.dismiss();
                        MessageBar.create(mContext, "如果想要支持我的话，可以在“关于”里面查看");
                    }
                })
                .create();
        view.findViewById(R.id.linear_layout_donation_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AlipayZeroSdk.hasInstalledAlipayClient(mContext)) {
                    AlipayZeroSdk.startAlipayClient(MainActivity.this, getResources().getString(R.string.alipay_code));
                } else {
                    MessageBar.create(mContext, "你可能没有安装支付宝");
                }
            }
        });
        view.findViewById(R.id.linear_layout_donation_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mContext.getString(R.string.qq_pay_code)));
                startActivity(intent);
            }
        });
        view.findViewById(R.id.linear_layout_donation_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mContext.getString(R.string.wechat_pay_code)));
                startActivity(intent);
            }
        });
        view.findViewById(R.id.linear_layout_grade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + mContext.getPackageName()));
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void create() {
        setSupportActionBar(mToolbar);
        mPresenter = new MainActivityPresenter(this);
        isRunning = false;
        if (!MediaUtils.hasNomediaFile()) {
            OkDownload.getInstance()
                    .addOnAllTaskEndListener(MainActivity.this);
        }
    }

    @Override
    protected void setFirstFragment() {
        if (!OtherConfig.getR(aContext)) {
            currentFragment = MoeimgFragment.newInstance(Net.MOEIMG_H);
        } else {
            currentFragment = MoeimgFragment.newInstance(Net.MOEIMG);
        }
        showFragment(currentFragment);
        setTitle(Menus.MENU_MOEIMG);
    }

    private void initDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withHeader(R.layout.drawer_header_view)
                .withHeaderDivider(false)
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        if (isRunning) return;
                        isRunning = true;
                        mPresenter.get();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .addDrawerItems(
                        new SecondaryDrawerItem().withName(Menus.MENU_ILLUSTRATION).withIdentifier(Menus.MENU_ILLUSTRATION_ID).withIcon(R.drawable.ic_menu_illustration).withSelectable(false),
                        new SecondaryDrawerItem().withName(Menus.MENU_MEIZI).withIdentifier(Menus.MENU_MEIZI_ID).withIcon(R.drawable.ic_menu_meizhi).withSelectable(false),
                        new SecondaryDrawerItem().withName(Menus.MENU_WALLPAPER).withIdentifier(Menus.MENU_WALLPAPER_ID).withIcon(R.drawable.ic_menu_photography).withSelectable(false),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(Menus.MENU_SETTINGS).withIdentifier(Menus.MENU_SETTINGS_ID).withIcon(R.drawable.ic_menu_settings).withSelectable(false),
                        new SecondaryDrawerItem().withName(Menus.MENU_ABOUT).withIdentifier(Menus.MENU_ABOUT_ID).withIcon(R.drawable.ic_menu_more).withSelectable(false)
                )
                .withSelectedItem(-1)
                .build();
        View header = drawer.getHeader();
        final KenBurnsView banner = header.findViewById(R.id.ken_burns_view_banner);
        String url = ThemeConfig.getBanner(mContext);
        try {
            if (url != null) {
                Uri uri = Uri.parse(url);
                if (uri != null) {
                    ImageRequest imageRequest = ImageRequestBuilder
                            .newBuilderWithSource(uri)
                            .build();
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    DataSource<CloseableReference<CloseableImage>>
                            dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
                    dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
                        @Override
                        protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                            mCloseableImageCloseableReference = dataSource.getResult();
                            if (mCloseableImageCloseableReference != null) {
                                CloseableImage image = mCloseableImageCloseableReference.get();
                                if (image instanceof CloseableBitmap) {
                                    CloseableBitmap closeableBitmap = (CloseableBitmap) image;
                                    Bitmap bitmap = closeableBitmap.getUnderlyingBitmap();
                                    if (bitmap == null) return;
                                    banner.setImageBitmap(bitmap);
                                }
                            }
                        }

                        @Override
                        protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

                        }
                    }, UiThreadImmediateExecutorService.getInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTextViewHitokoto = header.findViewById(R.id.text_view_juzi);
        mTextViewHitokotoSrc = header.findViewById(R.id.text_view_juzi_source);
    }

    private void initDrawerEvent() {
        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem dItem) {
                switch ((int) dItem.getIdentifier()) {
                    case Menus.MENU_SETTINGS_ID:
                        startActivity(new Intent(mContext, SettingsActivity.class));
                        break;
                    case Menus.MENU_ABOUT_ID:
                        startActivity(new Intent(mContext, MoreActivity.class));
                        break;
                    case Menus.MENU_ILLUSTRATION_ID:
                        currentFragment = new MenuIllustrationFragment();
                        setTitle(Menus.MENU_ILLUSTRATION);
                        break;
                    case Menus.MENU_MEIZI_ID:
                        currentFragment = new MenuMeiziFragment();
                        setTitle(Menus.MENU_MEIZI);
                        break;
                    case Menus.MENU_WALLPAPER_ID:
                        currentFragment = new MenuWallpaperFragment();
                        setTitle(Menus.MENU_WALLPAPER);
                        break;
                }
                showFragment(currentFragment);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (drawer != null) {
            drawer.closeDrawer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // searchView.setOnSearchClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         Toast.makeText(MainActivity.this,"Open",Toast.LENGTH_SHORT).show();
        //     }
        // });
        // searchView.setOnCloseListener(new SearchView.OnCloseListener() {
        //     @Override
        //     public boolean onClose() {
        //         Toast.makeText(MainActivity.this, "Close", Toast.LENGTH_SHORT).show();
        //         return false;
        //     }
        // });
        // 设置搜索文本监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                int type  = currSearchFragment.getFragmentMenuID();
                if(type == 0){
                    MessageBar.create(mContext, "此站点没有提供搜索");
                    return false;
                }
                String keywords = query;
                removeFragmentContainerChildrenViews();
                showFragment(SearchFragment.newInstance(type,keywords,"搜索:" + keywords));
                //清除焦点，收软键盘
                //mSearchView.clearFocus();
                return false;
            }
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_download_manager:
                startActivity(new Intent(mContext, DownloadManagerActivity.class));
                break;
            case R.id.action_change_span_count:
                EventBus.getDefault().postSticky(new RVBean(true, false));
                break;
            case R.id.action_touch_to_top:
                EventBus.getDefault().postSticky(new RVBean(false, true));
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.unsubscribe();
        }
        if (mCloseableImageCloseableReference != null) {
            CloseableReference.closeSafely(mCloseableImageCloseableReference);
            mCloseableImageCloseableReference = null;
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.frame_layout_fragment_container;
    }

    @Override
    public void getSuccess(JuZiBean juzi) {
        bindHitokoto(juzi);
        isRunning = false;
    }

    private void bindHitokoto(JuZiBean bean) {
        mTextViewHitokoto.setText(bean.getHitokoto());
        if (mTextViewHitokoto.getLineCount() > 1) {
            mTextViewHitokoto.setText(String.format("\u3000\u3000%s", bean.getHitokoto()));
        }
        mTextViewHitokotoSrc.setText(bean.getSource());
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public void onAllTaskEnd() {
        Observable.just(DownloadManager.getInstance().getFinished())
                .map(new Function<List<Progress>, String[]>() {
                    @Override
                    public String[] apply(@NonNull List<Progress> downloadTasks) throws Exception {
                        String[] paths = new String[downloadTasks.size()];
                        for (int i = 0; i < downloadTasks.size(); i++) {
                            paths[i] = downloadTasks.get(i).filePath;
                        }
                        return paths;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String[]>() {
                    @Override
                    public void accept(String[] paths) throws Exception {
                        MediaScannerConnection.scanFile(mContext, paths, null, null);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        MessageBar.create(mContext, "更新文件到媒体库时，发生了一些错误：" + throwable.getMessage());
                    }
                });

    }


    // @Override
    // protected void onNewIntent(Intent intent) {
    //     if (!intent.getAction().equals(Intent.ACTION_SEARCH)) {
    //         return;
    //     }
    //     int type  = currentFragment.getFragmentMenuID();
    //     if(type == 0){
    //         return;
    //     }
    //     String keywords = intent.getStringExtra(SearchManager.QUERY);
    //     removeFragmentContainerChildrenViews();
    //     showFragment(SearchFragment.newInstance(type,keywords,"搜索:" + keywords));
    // }

}


