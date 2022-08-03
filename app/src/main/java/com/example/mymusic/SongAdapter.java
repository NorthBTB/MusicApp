package com.example.mymusic;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.entity.Song;
import com.example.mymusic.utils.BindService;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private ISongAdapter iAdapter;

    private int level;
    private Handler handler;
    private Runnable runnable;
    private BindService bindService;

    public SongAdapter(Context context, ISongAdapter iAdapter) {
        this.iAdapter = iAdapter;
        this.context = context;
    }

    public class ViewHolderItem extends RecyclerView.ViewHolder {
        private ConstraintLayout ll_song_item;
        private TextView tv_song_name, tv_author;
        private ImageView iv_play_button;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            ll_song_item = itemView.findViewById(R.id.ll_item_song);
            iv_play_button = itemView.findViewById(R.id.iv_play_button);
            tv_song_name = itemView.findViewById(R.id.tv_song_name);
            tv_author = itemView.findViewById(R.id.tv_author);
        }
    }

//    public class ViewHolderHeader extends RecyclerView.ViewHolder {
//        private ConstraintLayout ctr_song_header;
//        private TextView tv_title;
//        private ImageView iv_add;
//
//        public ViewHolderHeader(@NonNull View itemView) {
//            super(itemView);
//            ctr_song_header = itemView.findViewById(R.id.ll_song_header);
//            tv_title = itemView.findViewById(R.id.tv_hsong_header);
//            iv_add = itemView.findViewById(R.id.iv_add);
//        }
//    }

//    public class ViewHolderFooter extends RecyclerView.ViewHolder {
//        private LinearLayout ll_song_footer;
//        private TextView tv_cr_song_name, tv_cr_author;
//        private ImageView iv_cr_play_button, iv_song_pic;
//
//        public ViewHolderFooter(@NonNull View itemView) {
//            super(itemView);
//            ll_song_footer = itemView.findViewById(R.id.ll_song_footer);
//            tv_cr_song_name = itemView.findViewById(R.id.tv_current_song_name);
//            tv_cr_author = itemView.findViewById(R.id.tv_current_author);
//            iv_cr_play_button = itemView.findViewById(R.id.iv_current_play_button);
//            iv_song_pic = itemView.findViewById(R.id.iv_song_pic);
//        }
//    }


    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            return RecycleViewType.HEADER;
//        }
//        if (position <= iAdapter.getSongs().size()) {
//            return RecycleViewType.ITEM;
//        }
        return RecycleViewType.ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case RecycleViewType.ITEM:
                return new com.example.mymusic.SongAdapter.ViewHolderItem(inflater.inflate(
                        R.layout.song_item,
                        parent,
                        false));
//            case RecycleViewType.HEADER:
//                return new com.example.mymusic.SongAdapter.ViewHolderHeader(inflater.inflate(
//                        R.layout.song_header,
//                        parent,
//                        false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {

//            case RecycleViewType.HEADER:
//                com.example.mymusic.SongAdapter.ViewHolderHeader viewHolderHeader = (com.example.mymusic.SongAdapter.ViewHolderHeader) holder;
//                int songPosition = position - 1;
//                viewHolderHeader.iv_add.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        iAdapter.onClickHeader();
//                    }
//                });
//                //ui
//                break;

            case RecycleViewType.ITEM:
                com.example.mymusic.SongAdapter.ViewHolderItem viewHolderItem = (com.example.mymusic.SongAdapter.ViewHolderItem) holder;
                // item
                int songPosition1 = position;
                Song song = iAdapter.getSongs().get(songPosition1);

                // event
                //pick a song
                viewHolderItem.ll_song_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iAdapter.onPlay(songPosition1);
                        //viewHolderItem.iv_play_button.setImageResource(R.drawable.sound_bg);

                    }
                });
                //remove
                viewHolderItem.ll_song_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        iAdapter.onLongClickItem(position, songPosition1);
                        return true;
                    }
                });

                // ui
                if (song.isPlaying()) {
                    viewHolderItem.iv_play_button.setImageResource(R.drawable.sound_bg);
                    level = 0;
                    handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            viewHolderItem.iv_play_button.setImageLevel(level);
                            if (level == 7) {
                                level = 0;
                            } else {
                                level++;
                            }
                            handler.postDelayed(this, 100);
                        }
                    };
                    handler.postDelayed(runnable, 100);
                } else {
                    viewHolderItem.iv_play_button.setImageResource(song.getPicId());
                }
                viewHolderItem.tv_song_name.setText(song.getSongName());
                viewHolderItem.tv_author.setText(song.getAuthor());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return iAdapter.getSongs().size();
    }

}
