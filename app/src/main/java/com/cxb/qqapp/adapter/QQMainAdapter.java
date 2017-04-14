package com.cxb.qqapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cxb.qqapp.R;
import com.cxb.qqapp.model.QQMessageBean;
import com.cxb.qqapp.utils.GlideCircleTransform;

import java.util.List;

/**
 * 主页列表适配器
 */

public class QQMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnListClickListener onListClickListener;

    private List<QQMessageBean> list;
    private LayoutInflater layoutInflater;

    private RequestManager requestManager;
    private GlideCircleTransform transform;

    public QQMainAdapter(Context context, List<QQMessageBean> list) {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);

        requestManager = Glide.with(context);
        transform = new GlideCircleTransform(context)
                .setColor(192, 192, 192, 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QQViewHolder(layoutInflater.inflate(R.layout.item_qq_main_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindNoHeadItem((QQViewHolder) holder, position);
    }

    private void bindNoHeadItem(QQViewHolder holder, final int position) {

        QQMessageBean qq = list.get(position);

        requestManager.load(qq.getAvatarRes())
                .transform(transform)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(qq.getAvatarRes())
                .error(qq.getAvatarRes())
                .into(holder.ivAvatar);

        holder.tvName.setText(qq.getName());
        holder.tvContent.setText(qq.getContent());
        holder.tvTime.setText(qq.getTime());

        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onListClickListener != null) {
                    onListClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class QQViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlItem;
        private ImageView ivAvatar;
        private TextView tvName;
        private TextView tvContent;
        private TextView tvTime;

        private QQViewHolder(View view) {
            super(view);
            rlItem = (RelativeLayout) view.findViewById(R.id.rl_item);
            ivAvatar = (ImageView) view.findViewById(R.id.iv_qq_avatar);
            tvName = (TextView) view.findViewById(R.id.tv_qq_name);
            tvContent = (TextView) view.findViewById(R.id.tv_qq_content);
            tvTime = (TextView) view.findViewById(R.id.tv_qq_time);
        }
    }

    public void setOnListClickListener(OnListClickListener onListClickListener) {
        this.onListClickListener = onListClickListener;
    }
}
