package com.prajwal.miniinsta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prajwal.miniinsta.Fragments.SearchFragment;
import com.prajwal.miniinsta.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mtags;
    private List<String> mtagsCount;

    public TagAdapter(Context mContext, List<String> mtags, List<String> mtagsCount) {
        this.mContext = mContext;
        this.mtags = mtags;
        this.mtagsCount = mtagsCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tags_item, parent, false);
        return new TagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tag.setText("#"+mtags.get(position));
        holder.noOfPosts.setText(mtagsCount.get(position) + "posts");

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  TextView tag;
        public TextView noOfPosts;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tag = itemView.findViewById(R.id.hashtag);
            noOfPosts = itemView.findViewById(R.id.no_of_posts);
        }
    }
    public void filter(List<String> filterTags, List<String> filterTagsCounts) {
        this.mtags = filterTags;
        this.mtagsCount = filterTagsCounts;
        notifyDataSetChanged();
    }
}
