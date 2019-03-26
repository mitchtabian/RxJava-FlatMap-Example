package com.codingwithmitch.rxjavaflatmapexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codingwithmitch.rxjavaflatmapexample.models.Post;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    private List<Post> posts = new ArrayList<>();
    private OnPostClickListener onPostClickListener;

    public RecyclerAdapter(OnPostClickListener onPostClickListener) {
        this.onPostClickListener = onPostClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_post_list_item, null, false);
        return new MyViewHolder(view, onPostClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void setPosts(List<Post> posts){
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void updatePost(Post post){
        posts.set(posts.indexOf(post), post);
        notifyItemChanged(posts.indexOf(post));
    }

    public List<Post> getPosts(){
        return posts;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnPostClickListener onPostClickListener;
        TextView title;

        public MyViewHolder(@NonNull View itemView, OnPostClickListener onPostClickListener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            this.onPostClickListener = onPostClickListener;

            itemView.setOnClickListener(this);
        }

        public void bind(Post post){
            title.setText(post.getTitle());

        }

        @Override
        public void onClick(View v) {
            onPostClickListener.onPostClick(getAdapterPosition());
        }
    }

    public interface OnPostClickListener{
        void onPostClick(int position);
    }
}






















