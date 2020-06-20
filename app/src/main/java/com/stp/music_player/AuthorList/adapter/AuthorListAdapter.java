package com.stp.music_player.AuthorList.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stp.music_player.R;

import java.util.ArrayList;

public class AuthorListAdapter extends RecyclerView.Adapter<AuthorListAdapter.AuthorViewHolder> {

    private ArrayList<String> mAuthorList;
    private Context mContext;
    OnAuthorSelected onAuthorSelected;

    public AuthorListAdapter(ArrayList<String> mAuthorList, Context mContext, OnAuthorSelected onAuthorSelected) {
        this.mAuthorList = mAuthorList;
        this.mContext = mContext;
        this.onAuthorSelected = onAuthorSelected;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.author_item, parent, false);
        return new AuthorViewHolder(view, onAuthorSelected);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        String author = mAuthorList.get(position);
        holder.mAuthorText.setText(author);
    }

    @Override
    public int getItemCount() {
        return mAuthorList.size();
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mAuthorText;
        OnAuthorSelected onAuthorSelected;

        public AuthorViewHolder(@NonNull View itemView, OnAuthorSelected onAuthorSelected) {
            super(itemView);
            this.onAuthorSelected = onAuthorSelected;
            itemView.setOnClickListener(this);
            mAuthorText = itemView.findViewById(R.id.author_tv);

        }

        @Override
        public void onClick(View view) {
            onAuthorSelected.onAuthorClicked(getAdapterPosition());
        }
    }

    public interface OnAuthorSelected{
        public void onAuthorClicked(int pos);
    }
}
