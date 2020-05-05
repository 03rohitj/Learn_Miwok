package com.example.android.miwok;

import android.app.Activity;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumbersFragment extends Fragment {

    ArrayList<Word> words;
    MediaPlayer mediaPlayer=null;
    AudioManager mAudioManager;
    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager=(AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        words = new ArrayList<>();
        words.add(new Word("one", "lutti",R.drawable.number_one,R.raw.number_one));
        words.add(new Word("two", "otiiko",R.drawable.number_two,R.raw.number_two));
        words.add(new Word("three", "tolookosu",R.drawable.number_three,R.raw.number_three));
        words.add(new Word("four", "oyyisa",R.drawable.number_four,R.raw.number_four));
        words.add(new Word("five", "massokka",R.drawable.number_five,R.raw.number_five));
        words.add(new Word("six", "temmokka",R.drawable.number_six,R.raw.number_six));
        words.add(new Word("seven", "kenekaku",R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("eight", "kawinta",R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("nine", "wo’e",R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("ten", "na’aacha",R.drawable.number_ten,R.raw.number_ten));

        WordAdapter adapter=new WordAdapter(getActivity(),0,words,R.color.category_numbers);
        ListView list=rootView.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Free up memory occupied by the previous MediaPlayer object which is no longer needed.
                int res=mAudioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(res == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    releaseMediaPlayer();
                    mediaPlayer=MediaPlayer.create(getActivity(),words.get(i).getSoundResourceId());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }


            }
        });
        return rootView;
    }
    AudioManager.OnAudioFocusChangeListener afChangeListener=new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch(focusChange)
            {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK :
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT :
                    if(mediaPlayer!=null)
                    {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo(0);
                    }

                    break;
                case AudioManager.AUDIOFOCUS_GAIN : if(mediaPlayer!=null)  mediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:              releaseMediaPlayer();
                    break;
            }

        }
    };

    MediaPlayer.OnCompletionListener onCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            releaseMediaPlayer();
        }
    };




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
            mAudioManager.abandonAudioFocus(afChangeListener);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
