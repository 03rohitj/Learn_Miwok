package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    MediaPlayer mediaPlayer = null;
    AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener afChangeListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch(focusChange)
            {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK :
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT :
                    if(mediaPlayer!=null) {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN :
                    if(mediaPlayer!=null)   mediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:              releaseMediaPlayer();
                    break;
            }

        }
    };


    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            releaseMediaPlayer();
            Toast.makeText(getActivity(), "MediaPlayer released? : " + (mediaPlayer == null), Toast.LENGTH_SHORT).show();
        }
    };

    public ColorsFragment() {
        // Required empty public constructor
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager=(AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> color = new ArrayList<>();
        color.add(new Word("weṭeṭṭi", "red", R.drawable.color_red, R.raw.color_red));
        color.add(new Word("chokki", "green", R.drawable.color_green, R.raw.color_green));
        color.add(new Word("takaakki", "brown", R.drawable.color_brown, R.raw.color_brown));
        color.add(new Word("topoppi", "gray", R.drawable.color_gray, R.raw.color_gray));
        color.add(new Word("kululli", "black", R.drawable.color_black, R.raw.color_black));
        color.add(new Word("kelelli", "white", R.drawable.color_white, R.raw.color_white));
        color.add(new Word("ṭopiisә", "dusty yellow", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        color.add(new Word("chiwiiṭә", "mustard yellow", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        WordAdapter adapter = new WordAdapter(getActivity(), 0, color, R.color.category_colors);
        ListView list = rootView.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //To play the audio file we have to request for AudioFocus
                int focusRes=mAudioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                //If system has granted the audio focus then only play sound
                if(focusRes == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //Free up memory occupied by the previous MediaPlayer object which is no longer needed.
                    releaseMediaPlayer();

                    mediaPlayer = MediaPlayer.create(getActivity(), color.get(i).getSoundResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
