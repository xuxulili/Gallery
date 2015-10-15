package com.data.gallery.gallery.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.data.gallery.gallery.R;
import com.data.gallery.gallery.adapter.MasonryAdapter;
import com.data.gallery.gallery.app;
import com.data.gallery.gallery.data.GetData;
import com.data.gallery.gallery.model.Picture;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2015/9/19.
 */
public class SceneryFragment extends Fragment{
    public static final  String  ARGUMENT = "argument";
    private String  url;
    private RecyclerView recyclerView_gallery;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MasonryAdapter masonryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Picture> allList;
    int type = 1;
    int page = 0;
    private boolean mIsRefreshing;
    private FloatingActionButton floatingActionButton;
    private View view;
    private  boolean hasLast = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Fragment newInstance(String type) {
        SceneryFragment fragment = new SceneryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT,type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.scenery_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle =getArguments();
        if(bundle!=null) {
            type = Integer.parseInt(bundle.getString(ARGUMENT));
        }
        allList = new ArrayList<>();
        initView();
        new MainPageAsyncTask().execute(0);
    }
    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                new MainPageAsyncTask().execute(0);
            }
        });
        recyclerView_gallery = (RecyclerView)view.findViewById(R.id.recyclerView);
        //设置layoutManager
//        recyclerView_gallery.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        linearLayoutManager = new GridLayoutManager(getActivity(),2);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_gallery.setLayoutManager(linearLayoutManager);
        //设置item之间的间隔
//        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
//        recyclerView_gallery.addItemDecoration(decoration);
        recyclerView_gallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mIsRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        recyclerView_gallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mCurrentState = RecyclerView.SCROLL_STATE_IDLE;
            private int lastdy = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mCurrentState == RecyclerView.SCROLL_STATE_DRAGGING || mCurrentState == RecyclerView.SCROLL_STATE_SETTLING) {

                    if (dy < 0) {//向下滑动

                        //可以不处理，在SwipeRefreshLayout的onRefreshListener中实现下拉刷新
                    } else {//向上滑动
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        if (layoutManager instanceof GridLayoutManager) {
                            int lastitem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                            if (lastitem > 30) {
                                floatingActionButton.setVisibility(View.VISIBLE);
                            }else{
                                if(hasLast){
                                    swipeRefreshLayout.setEnabled(true);
                                }

                            }

//                            if (lastitem<20) {
//                                floatingActionButton.setVisibility(View.INVISIBLE);
//                            }
                            if (recyclerView.getAdapter().getItemCount() == lastitem + 1) {
                                swipeRefreshLayout.setRefreshing(true);
                                swipeRefreshLayout.setEnabled(false);
                                new MainPageAsyncTask().execute(1);
                            }
                        }
                    }

                    lastdy = dy;
                }
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mCurrentState = newState;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.INVISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView_gallery.scrollToPosition(0);
                floatingActionButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    class MainPageAsyncTask extends AsyncTask<Integer, Void, List<Picture>> {

        @Override
        protected List<Picture> doInBackground(Integer... integers) {

            if (integers[0] == 0) {
//                type = 1;
                page = 1;
            } else {
//                type = 1;
                page++;
                Log.e("加载下一页", page + "");
            }
            try {
////                GetData.readString("http://geek.csdn.net/service/news/get_news_list?jsonpcallback=jQuery20306760170501388163_1442678663112&username=&from=-&size=20&type=hackernewsv2_new&_=1442678663113");
////                GetData.readString("http://job.cqupt.edu.cn/main/jobClnd/2015-10");
////                GetData.readString("http://api.map.baidu.com/telematics/v3/weather?location=武汉&output=json&ak=B95329fb7fdda1e32ba3e3a245193146");
//                GetData.readString("http://job.cqupt.edu.cn/main/jobClnd/2015-10");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<com.data.gallery.gallery.model.Picture> list = new ArrayList<>();
            list = GetData.getPictures(type, page);
            return list;
        }

        @Override
        protected void onPostExecute(List<Picture> pictures) {
            super.onPostExecute(pictures);
            if (pictures.isEmpty()) {

                Log.e("输出为空", "11");
                if(!hasLast){
                    Toast.makeText(app.getContext(), "没有更多了", Toast.LENGTH_SHORT).show();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setEnabled(true);

                    }
                    hasLast=true;
                }
                swipeRefreshLayout.setRefreshing(false);
                return;
            }

            if (page == 1) {
                allList.clear();
                allList.addAll(pictures);
            } else {
                allList.addAll(pictures);
            }
            mIsRefreshing = false;
            if (masonryAdapter == null) {
                masonryAdapter = new MasonryAdapter(allList,getActivity());
                recyclerView_gallery.setAdapter(masonryAdapter);
            } else {
                masonryAdapter.notifyDataSetChanged();
            }
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
