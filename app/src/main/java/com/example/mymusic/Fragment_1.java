package com.example.mymusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymusic.entity.Song;
import com.example.mymusic.utils.BindService;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class Fragment_1 extends Fragment implements ISongAdapter, View.OnClickListener, IFragment1 {

    private View mView;
    private RecyclerView rv_song;
    private SongAdapter adapter;
    private List<Song> songs;
    private EditText et_search;
    private TextView tv_cr_song_name, tv_cr_author, tv_stop;
    private ImageView iv_song_pic, iv_cr_play, iv_play, iv_next, iv_pre;
    private int current_song = 0, cr_playing_song = 0, cr_playing_second = 0;

    private LinearLayout ll_song_footer;

    private BindService bindService;
    private boolean isServiceConnected;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BindService.MyBinder myBinder = (BindService.MyBinder) iBinder;
            bindService = myBinder.getService();
            isServiceConnected = true;

            onPlay(current_song);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bindService = null;
            isServiceConnected = false;
        }
    };

    public Fragment_1() {
        // Required empty public constructor

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_1, container, false);

        rv_song = mView.findViewById(R.id.rcv_song);

        et_search = mView.findViewById(R.id.et_search);
        tv_cr_song_name = mView.findViewById(R.id.tv_current_song_name);
        tv_cr_author = mView.findViewById(R.id.tv_current_author);
        tv_stop = mView.findViewById(R.id.tv_stop_music);
        iv_song_pic = mView.findViewById(R.id.iv_song_pic);
        iv_cr_play = mView.findViewById(R.id.iv_current_play_button);
        iv_next = mView.findViewById(R.id.iv_next);
        iv_pre = mView.findViewById(R.id.iv_pre);

        ll_song_footer = mView.findViewById(R.id.ll_song_footer);

        initSongs();

        iv_song_pic.setImageResource(songs.get(0).getPicId());
        tv_cr_song_name.setText(songs.get(0).getSongName());
        tv_cr_author.setText(songs.get(0).getAuthor());

        adapter = new SongAdapter(getActivity(), this);
        rv_song.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_song.setAdapter(adapter);
        rv_song.setItemAnimator(new SlideInLeftAnimator());


        iv_cr_play.setOnClickListener(this);
        iv_pre.setOnClickListener(this);
        iv_next.setOnClickListener(this);
        tv_stop.setOnClickListener(this);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                initSongs();
                List<Song> ss = new ArrayList<>(songs);
                songs.clear();
                if (s.isEmpty()) {
                    initSongs();
                } else {
                    s = s.toLowerCase();
                    for (int i = 0; i < ss.size(); i++) {
                        // Adapt the if for your usage
                        if (ss.get(i).getSongName().toLowerCase().contains(s)) {
                            songs.add(ss.get(i));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        ll_song_footer.setOnClickListener(this);

        return mView;
    }

    private void initSongs() {
        songs = new ArrayList<>();
        songs.add(new Song(R.drawable.mtp, R.raw.con_mua_ngang_qua, "Cơn mưa ngang qua", "Sơn Tùng-MTP", false));
        songs.add(new Song(R.drawable.mood, R.raw.mood, "Mood", "24KGoldn ft. Iann Dior", false));
        songs.add(new Song(R.drawable.mtp, R.raw.hay_trao_cho_anh, "Hãy trao cho anh", "Sơn Tùng-MTP", false));
        songs.add(new Song(R.drawable.fools_garden, R.raw.lemon_tree, "Lemon Tree", "Fool's Gardeen", false));
        songs.add(new Song(R.drawable.maroon_5, R.raw.sugar, "Sugar", "Maroon 5", false));
        songs.add(new Song(R.drawable.justin_bieber, R.raw.sorry, "Sorry", "Justin Bieber", false));
        songs.add(new Song(R.drawable.justin_bieber, R.raw.mood, "Baby", "Justin Bieber", false));
        songs.add(new Song(R.drawable.image_1, R.raw.mood, "Em gái mưa", "Hương Tràm", false));
        songs.add(new Song(R.drawable.image_2, R.raw.mood, "Hơn cả yêu", "Dức Phúc", false));
        songs.add(new Song(R.drawable.image_3, R.raw.mood, "Simple Cypher", "Low G", false));
        songs.add(new Song(R.drawable.image_4, R.raw.mood, "Chán gái 707", "Low G", false));
        songs.add(new Song(R.drawable.image_5, R.raw.mood, "Không quen", "Chí", false));

    }

    @Override
    public List<Song> getSongs() {
        return songs;
    }

    @Override
    public void onLongClickItem(int position, int songPosition) {
        new AlertDialog.Builder(getActivity())
                .setMessage("Do you want to remove '" + getSongs().get(songPosition).getSongName() + "'?")
                .setCancelable(false)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getSongs().remove(songPosition);
                        adapter.notifyItemRemoved(position);
                    }
                })
                .setPositiveButton("No", null)
                .show();
    }

    @Override
    public void onClickFooter(int songPositon) {

    }

    @Override
    public void onClickHeader() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_current_play_button:
                onPlayCurrentSong();
                break;

            case R.id.ll_song_footer:
                viewSongDetail();
                break;

            case R.id.iv_next:
                onNext();
                break;

            case R.id.iv_pre:
                onPre();
                break;

            case R.id.tv_stop_music:
                for(Song s:songs){
                    s.setPlaying(false);
                    s.setPlayed(false);
                }
                onClickStopService();
                break;
        }
    }

    private void viewSongDetail() {
        Intent intent = new Intent(getActivity(), LyricActivity.class);

        intent.putExtra("cr_song", current_song);
        intent.putExtra("cr_playing_song", cr_playing_song);
        intent.putExtra("is_played", songs.get(current_song).isPlayed());
        startActivity(intent);
    }

    @Override
    public void onPlayCurrentSong() {
        setStatusPlay();
        if(bindService==null){
            //onClickStartService();
        }
    }

    @Override
    public void onPlay(int songPosition) {
        current_song = songPosition;
        Song cr_song = getSongs().get(current_song);
        if (cr_song.isPlayed()) {
            iv_cr_play.setImageResource(R.drawable.ic_baseline_pause_24);

        } else {
            iv_cr_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }
        iv_song_pic.setImageResource(cr_song.getPicId());
        tv_cr_song_name.setText(cr_song.getSongName());
        tv_cr_author.setText(cr_song.getAuthor());
    }

    @Override
    public void onNext() {
        onClickStopService();
        if (current_song == songs.size() - 1) {
            current_song = 0;
        } else {
            current_song++;
        }
        onPlayCurrentSong();

        onPlay(current_song);
    }

    @Override
    public void onPre() {
        onClickStopService();
        if (current_song == 0) {
            current_song = songs.size() - 1;
        } else {
            current_song--;
        }
        onPlayCurrentSong();
        onPlay(current_song);
    }

    //service
    private void onClickStartService() {
        Intent intent = new Intent(getActivity(), BindService.class);
        Bundle bundle = new Bundle();
        Song cr_song = songs.get(current_song);
        bundle.putSerializable("cr_song", cr_song);
        bundle.putInt("cr",current_song);
        intent.putExtras(bundle);
        getActivity().startService(intent);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void onClickStopService() {
        iv_cr_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        adapter.notifyDataSetChanged();
        Intent intent = new Intent(getActivity(), BindService.class);
        getActivity().stopService(intent);
        if (isServiceConnected) {
            getActivity().unbindService(serviceConnection);
            isServiceConnected = false;
        }
    }

    //update status when play or pause
    private void setStatusPlay() {
        Song cr_song = getSongs().get(current_song);

        for (Song s : songs) {
            if (s != cr_song) {
                s.setPlayed(false);
                s.setPlaying(false);
            }
        }

        cr_song.setPlaying(!cr_song.isPlaying());
        if (cr_song.isPlayed() == false) {
            cr_song.setPlayed(true);
            cr_song.setPlaying(true);
            iv_cr_play.setImageResource(R.drawable.ic_baseline_pause_24);
            onClickStopService();
            onClickStartService();
        } else {
            if (bindService.isPlaying()) {
                iv_cr_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                cr_song.setPlaying(false);
                bindService.onPause();
            } else {
                iv_cr_play.setImageResource(R.drawable.ic_baseline_pause_24);
                cr_song.setPlaying(true);
                bindService.onResume();
            }
        }
        adapter.notifyDataSetChanged();
    }
}