package com.gofreshuser.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Sub_catmodel;
import com.gofreshuser.tecmanic.R;

import java.util.List;

public class Sub_CatAdapter extends RecyclerView.Adapter<Sub_CatAdapter.MyViewHolder> {

    private List<Sub_catmodel> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        Button button;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);

        }
    }

    public Sub_CatAdapter(List<Sub_catmodel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Sub_CatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_show_sub_cat, parent, false);

        context = parent.getContext();

        return new Sub_CatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Sub_CatAdapter.MyViewHolder holder, final int position) {
        Sub_catmodel mList = modelList.get(position);


        Glide.with(context)
                .load(Baseurl.IMG_CATEGORY_URL + mList.getImage())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);



        holder.title.setText(mList.getTitle());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

