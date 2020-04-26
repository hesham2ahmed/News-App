package com.hesham.newsapp;


import android.app.LoaderManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private int mNumberNews;
    private List<News> news;

    public interface ListItemClickListener extends LoaderManager.LoaderCallbacks<List<News>> {
        void onListItemClick(int position);
    }
    
    final private ListItemClickListener mOnClickListener;
    

    public NewsAdapter(List<News> news, int mNumberNews, ListItemClickListener listener){
        this.news = news;
        this.mNumberNews = mNumberNews;
        mOnClickListener = listener;
    }

    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        NewsAdapter.NewsViewHolder viewHolder = new NewsAdapter.NewsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, int position) {
        holder.bind(position, news);
    }

    @Override
    public int getItemCount() {return mNumberNews;}

    public void clear() {
        int size = news.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                news.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    public class NewsViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView news_title;
        TextView news_section_name;
        TextView news_type;
        TextView news_author_name;
        TextView news_location;
        TextView news_date;
        TextView news_time;

        public NewsViewHolder(View itemView) {
            super(itemView);
            news_title = itemView.findViewById(R.id.news_title);
            news_section_name = itemView.findViewById(R.id.news_section_name);
            news_type = itemView.findViewById(R.id.news_type);
            news_author_name = itemView.findViewById(R.id.news_author_name);
            news_date = itemView.findViewById(R.id.news_date);
            news_time = itemView.findViewById(R.id.news_time);
            itemView.setOnClickListener(this);
        }

        public void bind(int position, List<News> news){
            news_title.setText(news.get(position).getTitle());
            news_section_name.setText(news.get(position).getSection_name());
            news_type.setText(news.get(position).getType());
            news_author_name.setText(news.get(position).getAuthor_name());


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Instant instant = Instant.parse( news.get(position).getPublication_date() );
                java.util.Date date = java.util.Date.from( instant );
                news_date.setText(formatDate(date));
                news_time.setText(formatTime(date));
            }
        }

        /**
         * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
         */
        private String formatDate(Date dateObject) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
            return dateFormat.format(dateObject);
        }

        /**
         * Return the formatted date string (i.e. "4:30 PM") from a Date object.
         */
        private String formatTime(Date dateObject) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            return timeFormat.format(dateObject);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }



}
