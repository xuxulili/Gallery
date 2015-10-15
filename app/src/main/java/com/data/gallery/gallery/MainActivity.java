package com.data.gallery.gallery;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.data.gallery.gallery.adapter.MasonryAdapter;
import com.data.gallery.gallery.data.GetData;
import com.data.gallery.gallery.model.Picture;
import com.data.gallery.gallery.ui.SceneryFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView_gallery;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MasonryAdapter masonryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Picture> allList;
    int type = 0;
    int page = 0;
    private boolean mIsRefreshing;
    private FloatingActionButton floatingActionButton;
    private Toolbar toolbar;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private android.support.v7.app.ActionBarDrawerToggle mActionBarDrawerToggle;
    private AppBarLayout appBarLayout;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        initToolBar();
        initNavigationView();
        replaceFragment(R.id.id_content, SceneryFragment.newInstance("1"));
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        linearLayout = (LinearLayout) findViewById(R.id.main_content);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintResource(R.color.blue);
            tintManager.setStatusBarTintEnabled(true);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            linearLayout.setPadding(0, config.getStatusBarHeight(), 0, config.getPixelInsetBottom());
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            toolbar.setTitle(getString(R.string.title));
            actionBar.setDisplayHomeAsUpEnabled(true);//给左上角图标的左边加上一个返回的图标
            actionBar.setDisplayShowHomeEnabled(true);   //使左上角图标是否显示，如果设成false，则没有程序
            // 图标，仅仅就个标题，否则，显示应用程序图标，对应id为android.R.id.home，
            actionBar.setDisplayShowCustomEnabled(true);  // 使自定义的普通View能在title栏显示，
            // 即actionBar.setCustomView能起作用
            actionBar.setDisplayShowTitleEnabled(true);   //对应ActionBar.DISPLAY_SHOW_TITLE。
//            actionBar.setLogo(R.drawable.ic_discuss);
        }

    }

    private void initNavigationView() {

        mNavigationView = (NavigationView) findViewById(R.id.id_nv_menu);
        if (mNavigationView != null) {
            setupContentDrawer(mNavigationView);
        }
        //为setNavigationIcon设置动作
        if (mActionBarDrawerToggle == null) {
            mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                    mDrawerLayout, toolbar, R.string.open, R.string.close);
            mActionBarDrawerToggle.syncState();
        }
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    private void setupContentDrawer(NavigationView mNavigationView) {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.scenery:
                        replaceFragment(R.id.id_content, SceneryFragment.newInstance("1"));
                        break;
                    case R.id.humanities:
                        replaceFragment(R.id.id_content, SceneryFragment.newInstance("2"));
                        break;
                    case R.id.Plant:
                        replaceFragment(R.id.id_content, SceneryFragment.newInstance("7"));
                        break;
                    case R.id.Aerial:
                        replaceFragment(R.id.id_content, SceneryFragment.newInstance("3"));
                        break;
                    case R.id.under_water:
                        replaceFragment(R.id.id_content, SceneryFragment.newInstance("4"));
                        break;
//                    case R.id.theme:
//                        setTheme(R.style.BlueTheme);
//                        break;
                    case R.id.exit:
                        finish();
                        break;
                }
                return true;
            }
        });
    }


    public void replaceFragment(int id_content, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id_content, fragment);
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AndroidShare as = new AndroidShare(
                    MainActivity.this,
                    "哈哈---超方便的分享！！！来自allen",
                    "http://img6.cache.netease.com/cnews/news2012/img/logo_news.png");
            as.show();
            return true;
        }
        if (item.getItemId() == android.R.drawable.ic_input_delete) {
            //打开抽屉侧滑菜单
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("退出"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
//        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if ((System.currentTimeMillis() - exitTime) > 2000) {
//                        exitTime = System.currentTimeMillis();//返回当前时间至毫秒
//                    } else {
                    dialog.dismiss();
                    moveTaskToBack(false);
//                finish();
//                    }

                }
                return true;
            }
        });
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                finish();
//                Toast.makeText(app.getContext(), "确认" + which, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(app.getContext(), "取消" + which, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("后台运行", new DialogInterface.OnClickListener() {//设置忽略按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                moveTaskToBack(false);//后台运行实现
//                Toast.makeText(app.getContext(), "后台运行" + which, Toast.LENGTH_SHORT).show();
            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showExitDialog();
                Toast.makeText(app.getContext(), "再按一次后台运行！", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
