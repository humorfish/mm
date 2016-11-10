package com.you.mm.page.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.you.mm.R;
import com.you.mm.bean.Meizhi;
import com.you.mm.widget.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/11/10.
 */
public class MmListAdapter extends RecyclerView.Adapter<MmListAdapter.ViewHolder> {
    private OnMeizhiTouchListener mOnMeizhiTouchListener;
    List<Meizhi> mList;
    Context mContext;

    public MmListAdapter(Context context, List<Meizhi> meizhiList) {
        mList = meizhiList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_mm, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Meizhi meizhi = mList.get(position);
        int limit = 48;
        String text = meizhi.desc.length() > limit ? meizhi.desc.substring(0, limit) + "..." : meizhi.desc;
        holder.meizhi = meizhi;
        holder.titleView.setText(text);
        holder.card.setTag(meizhi.desc);

        Glide.with(mContext).load(meizhi.url).centerCrop().into(holder.meizhiView).getSize((width, height) -> {
            if (holder.card.isShown()){
                holder.card.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnMeizhiTouchListener {
        void onTouch(View v, View meizhiView, View card, Meizhi meizhi);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_meizhi)
        RatioImageView meizhiView;
        @BindView(R.id.tv_title)
        TextView titleView;
        View card;
        Meizhi meizhi;

        public ViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            ButterKnife.bind(this, itemView);
            meizhiView.setOnClickListener(this);
            card.setOnClickListener(this);
            meizhiView.setOriginalSize(50, 50);
        }


        @Override public void onClick(View v) {
            mOnMeizhiTouchListener.onTouch(v, meizhiView, card, meizhi);
        }
    }


    public void setmOnMeizhiTouchListener(OnMeizhiTouchListener mOnMeizhiTouchListener) {
        this.mOnMeizhiTouchListener = mOnMeizhiTouchListener;
    }
}
