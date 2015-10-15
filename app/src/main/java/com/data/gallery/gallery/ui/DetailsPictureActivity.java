package com.data.gallery.gallery.ui;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.data.gallery.gallery.R;
import com.data.gallery.gallery.app;
import com.data.gallery.gallery.data.GetData;
import com.data.gallery.gallery.model.Picture;
import com.data.gallery.gallery.model.PictureDetails;
import com.data.gallery.gallery.ui.view.ImgTouchView;
import com.data.gallery.gallery.utils.NetWorkUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/9/17.
 */
public class DetailsPictureActivity extends SwipeBackActivity {
    private List<PictureDetails> detailsList;
    private  String  url_details;
    private  String  title_details;
    private ViewPager viewPager;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private LinearLayout linearLayout_text;
    private TextView textView_content;
    private boolean hideText = true;
    private TextView textView_page;
    private  TextView textView_first_page;
    private LinearLayout text_first_layout;
    private android.support.v7.app.ActionBar actionBar;
    private AppBarLayout appBarLayout;
    private int cur_postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.details_picture);
//        //去除title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏

        Log.e("详情页启动", "");
        detailsList = new ArrayList<>();
        url_details = getIntent().getExtras().getString("detail_url");
        title_details = getIntent().getExtras().getString("detail_title");
        Log.e("1", url_details);
        initToolbar();
        initView();
        initImageLoader();
        new DetailsPictureTask().execute();
    }
//    private LinearLayout linearLayout;
    private void initToolbar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
//        linearLayout = (LinearLayout) findViewById(R.id.main_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//增加返回图标
        actionBar.setTitle(title_details);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintResource(R.color.blue);
//            tintManager.setStatusBarTintEnabled(true);
//            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
//            linearLayout.setPadding(0, config.getStatusBarHeight(), 0, config.getPixelInsetBottom());
//        }
    }
    private void initView(){
        viewPager = (ViewPager) findViewById(R.id.details_picture);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        textView_content = (TextView) findViewById(R.id.details_picture_text);
        textView_page = (TextView) findViewById(R.id.page);
        textView_first_page = (TextView) findViewById(R.id.first_page_text);
        linearLayout_text = (LinearLayout) findViewById(R.id.text_layout);
//        appBarLayout.getBackground().setAlpha(150);
        linearLayout_text.getBackground().setAlpha(150);
        text_first_layout = (LinearLayout) findViewById(R.id.text_first_layout);
        text_first_layout.getBackground().setAlpha(150);
        text_first_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text_first_layout.setVisibility(View.GONE);
            }
        });
    }
    private void initImageLoader(){
        imageLoader = ImageLoader.getInstance();

        int loadingResource = R.drawable.ic_loading;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
//                .showImageOnLoading(loadingResource)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class  DetailsAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener{
        private List<PictureDetails> list;
        public DetailsAdapter(List<PictureDetails> detailsList){
            list=detailsList;
        }
        @Override
        public int getCount() {
            return list.size()-1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==(View)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.item_details_picture,container,false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            if(position==0){
                textView_page.setText((position+1)+"/"+getCount());
                textView_content.setText(list.get(position + 1).getImageDetail_text());
            }
            if(list!=null){

                imageLoader.displayImage(list.get(position + 1).getImageDetails(), imageView, options);
//                Toast.makeText(app.getContext(),imageLoader.getDiskCache().getDirectory()+"  "+ imageLoader.getDiskCache().getDirectory().getName(),Toast.LENGTH_LONG).show();
                Log.e("缓存图片", imageLoader.getDiskCache().getDirectory() + "  " + imageLoader.getDiskCache().getDirectory().getName());
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!hideText) {
                            Log.e("viewpager被点击啦", "");
                            Animation animation_one = AnimationUtils.loadAnimation(app.getContext(), R.anim.text_slide_out);
                            Animation animation_two = AnimationUtils.loadAnimation(app.getContext(), R.anim.toolbar_out);
                            linearLayout_text.setAnimation(animation_one);
                            appBarLayout.setAnimation(animation_two);
                            appBarLayout.setVisibility(View.GONE);
                            linearLayout_text.setVisibility(View.GONE);
                            hideText = true;
                        } else {
                            Animation animation_one = AnimationUtils.loadAnimation(app.getContext(), R.anim.text_slide_in);
                            Animation animation_two = AnimationUtils.loadAnimation(app.getContext(), R.anim.toolbar_in);
                            linearLayout_text.setAnimation(animation_one);
                            appBarLayout.setAnimation(animation_two);
                            appBarLayout.setVisibility(View.VISIBLE);
                            linearLayout_text.setVisibility(View.VISIBLE);
                            hideText = false;
                        }

                    }
                });
            }
            container.addView(view);
            return view;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            cur_postion = position;
            textView_content.setText(list.get(position + 1).getImageDetail_text());
            textView_page.setText((position + 1) + "/" + getCount());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class DetailsPictureTask extends AsyncTask<String,Void,List<PictureDetails>>{
        @Override
        protected List<PictureDetails> doInBackground(String... strings) {
            List<PictureDetails> list = null;
            if(NetWorkUtil.isNetWorkConnected(app.getContext())) {
                list = GetData.getPictureDetails(url_details);
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<PictureDetails> pictureDetailses) {
            super.onPostExecute(pictureDetailses);
            if(pictureDetailses!=null&&pictureDetailses.size()>0){
                detailsList.addAll(pictureDetailses);
                String first_page_text = detailsList.get(0).getImageDetail_text();
                if(!first_page_text.isEmpty()&&first_page_text!=""){
                    textView_first_page.setText(first_page_text);
                }else{
                    text_first_layout.setVisibility(View.INVISIBLE);
                }

                DetailsAdapter detailsAdapter = new DetailsAdapter(detailsList);
                viewPager.setAdapter(detailsAdapter);
                viewPager.setOnPageChangeListener(detailsAdapter);
            }

            Log.e("111", pictureDetailses.get(0).getImageDetail_text() + pictureDetailses.get(0).getImageDetails());
            Log.e("112", pictureDetailses.get(1).getImageDetail_text() + pictureDetailses.get(1).getImageDetails());
        }
    }
}
