package com.example.wu.treeview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu on 2016/6/6.
 */
public abstract class TreeListAdapter<T> extends RecyclerView.Adapter<ViewHolder> implements View.OnClickListener {
    private List<Node> mAllNodes = new ArrayList<Node>();
    private List<Node> mVisibleNodes = new ArrayList<Node>();
    private int defaultExpandLevel;
    private Context mContext;
    private  OnRecyclerViewListener mListener;
    public TreeListAdapter(Context context, List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        this.mContext = context;
        this.mAllNodes = TreeHelpUtil.getStortedNodes(datas, defaultExpandLevel);
        this.mVisibleNodes = TreeHelpUtil.filterVisiableNode(mAllNodes);
        this.defaultExpandLevel = defaultExpandLevel;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=geView(parent,viewType);

        ViewHolder holder =new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    public abstract View geView(ViewGroup parent,int viewType);

    @Override
    public abstract void onBindViewHolder(ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mVisibleNodes.size();
    }

    public void setmListener(OnRecyclerViewListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onClick(View view) {
        if(mListener!=null){
            expandOrCollpse((int)view.getTag());
            mListener.onItemClick(view, (String) view.getTag());
        }
    }

    /**
     * 根据postion改变开关状态
     * @param position
     */
    private void expandOrCollpse(int position) {
        Node node=mVisibleNodes.get(position);
        if(node!=null){
            if(node.isLeaf()){
                return;
            }
            node.setExpand(!node.isExpand());
            mVisibleNodes=TreeHelpUtil.filterVisiableNode(mAllNodes);
            notifyDataSetChanged();
        }
    }




    public static interface OnRecyclerViewListener {
        void onItemClick(View view , String data);
    }
}
