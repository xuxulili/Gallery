package com.data.gallery.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.data.gallery.gallery.R;
import com.data.gallery.gallery.app;
import com.data.gallery.gallery.model.Picture;
import com.data.gallery.gallery.ui.DetailsPictureActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2015/9/14.
 */
public class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView>{
    private List<Picture> Pictures;
    private ImageLoader imageLoader;
    private DisplayImageOptions  options;
    private Context context;
    public MasonryAdapter(List<Picture> list,Context context) {
        Pictures=list;
        this.context = context;
        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        int loadingResource = R.drawable.img1;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(loadingResource)
                .build();
    }

    @Override
    public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_item, viewGroup, false);
        return new MasonryView(view);
    }

    @Override
    public void onBindViewHolder(MasonryView masonryView, final int position) {
        final int pos = position;
//        masonryView.imageView.setImageResource(Pictures.get(position).getImgUrl());
        imageLoader.displayImage(Pictures.get(position).getImgUrl(),masonryView.imageView,options);
        masonryView.textView_title.setText(Pictures.get(position).getTitle());
        masonryView.textView_time.setText(Pictures.get(position).getDate());
        masonryView.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailsPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("detail_url", Pictures.get(pos).getUrl());
                bundle.putString("detail_title", Pictures.get(pos).getTitle());
                Log.e("detail_url11",Pictures.get(pos).getUrl());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Pictures.size();
    }

     class MasonryView extends  RecyclerView.ViewHolder{
         CardView cardView;
        ImageView imageView;
        TextView textView_title;
         TextView textView_time;

        public MasonryView(View itemView){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card);
            imageView= (ImageView) itemView.findViewById(R.id.gallery_image );
            textView_title= (TextView) itemView.findViewById(R.id.gallery_title);
            textView_time= (TextView) itemView.findViewById(R.id.gallery_time);
        }

    }

}