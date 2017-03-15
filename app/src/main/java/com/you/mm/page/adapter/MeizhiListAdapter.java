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
 * Created by Administrator on 2017/2/27.
 */

public class MeizhiListAdapter
        extends RecyclerView.Adapter<MeizhiListAdapter.ViewHolder>
{

    public static final String TAG = "MeizhiListAdapter";

    private List<Meizhi> mList;
    private Context mContext;
    private OnMeizhiTouchListener mOnMeizhiTouchListener;

    public MeizhiListAdapter(Context context)
    {
        mContext = context;
    }

    public void setDatas(List<Meizhi> meizhiList)
    {
        mList = meizhiList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mm, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        Meizhi meizhi = mList.get(position);
        int limit = 48;
        String text = meizhi.desc.length() > limit ? meizhi.desc.substring(0, limit) +
                "..." : meizhi.desc;
        viewHolder.meizhi = meizhi;
        viewHolder.titleView.setText(text);
        viewHolder.card.setTag(meizhi.desc);

        Glide.with(mContext)
                .load(meizhi.url)
                .centerCrop()
                .into(viewHolder.meizhiView)
                .getSize((width, height) -> {
                    if (!viewHolder.card.isShown())
                    {
                        viewHolder.card.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onViewRecycled(ViewHolder holder)
    {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }


    public void setOnMeizhiTouchListener(OnMeizhiTouchListener onMeizhiTouchListener)
    {
        this.mOnMeizhiTouchListener = onMeizhiTouchListener;
    }

    interface OnMeizhiTouchListener
    {
        void onTouch(View view, RatioImageView RameizhiView, View card, Meizhi meizhi);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.meizhi)
        RatioImageView meizhiView;
        @BindView(R.id.title)
        TextView titleView;
        View card;
        Meizhi meizhi;


        public ViewHolder(View itemView)
        {
            super(itemView);
            card = itemView;
            ButterKnife.bind(this, itemView);
            meizhiView.setOnClickListener(this);
            card.setOnClickListener(this);
            meizhiView.setOriginalSize(50, 50);
        }


        @Override
        public void onClick(View v)
        {
            mOnMeizhiTouchListener.onTouch(v, meizhiView, card, meizhi);
        }
    }
}
