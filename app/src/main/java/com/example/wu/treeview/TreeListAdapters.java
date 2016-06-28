package com.example.wu.treeview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/6/7.
 */
public class TreeListAdapters<T> extends RecyclerView.Adapter<TreeListAdapters.MyViewHolder> implements View.OnClickListener {
    private List<Node> mAllNodes = new ArrayList<Node>();
    private List<Node> mVisibleNodes = new ArrayList<Node>();
    private int defaultExpandLevel=1;
    private Context mContext;
    private OnRecyclerViewListener mListener;

    public TreeListAdapters(Context context, List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        this.mContext = context;
        this.mAllNodes = TreeHelpUtil.getStortedNodes(datas, defaultExpandLevel);
        this.mVisibleNodes = TreeHelpUtil.filterVisiableNode(mAllNodes);
        this.defaultExpandLevel = defaultExpandLevel;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Node node = mVisibleNodes.get(position);
        holder.iv.setImageResource(node.getIcon());
        holder.tv.setText(node.getName());
        holder.itemView.setPadding(30 * position, 0, 0, 0);
        holder.itemView.setTag(position);
    }






    @Override
    public int getItemCount() {
        return mVisibleNodes.size();
    }

    public void setmListener(OnRecyclerViewListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            expandOrCollpse((int) view.getTag());
            mListener.onItemClick(view, (String) view.getTag());
        }
    }

    /**
     * 根据postion改变开关状态
     *
     * @param position
     */
    private void expandOrCollpse(int position) {
        Node node = mVisibleNodes.get(position);
        if (node != null) {
            if (node.isLeaf()) {
                return;
            }
            node.setExpand(!node.isExpand());
            mVisibleNodes = TreeHelpUtil.filterVisiableNode(mAllNodes);
            notifyDataSetChanged();
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }

    public static interface OnRecyclerViewListener {
        void onItemClick(View view, String data);
    }
}
