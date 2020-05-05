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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhrasesFragment extends Fragment {

    MediaPlayer mediaPlayer=null;
    AudioManager mAudioManager;

    MediaPlayer.OnCompletionListener onCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            releaseMediaPlayer();
        }
    };

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch(focusChange)
            {
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK :
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT :   if(mediaPlayer!=null) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }

                    break;
                case AudioManager.AUDIOFOCUS_GAIN : if(mediaPlayer!=null) mediaPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:              releaseMediaPlayer();
                    break;
            }

        }
    };

    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.word_list, container, false);
        mAudioManager= (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> phrases=new ArrayList<>();
        phrases.add(new Word("minto wuksus","Where are you going?",R.raw.phrase_where_are_you_going));
        phrases.add(new Word("tinnә oyaase'nә","What is your name?",R.raw.phrase_what_is_your_name));
        phrases.add(new Word("oyaaset...","My name is...",R.raw.phrase_my_name_is));
        phrases.add(new Word("michәksәs?","How are you feeling?",R.raw.phrase_how_are_you_feeling));
        phrases.add(new Word("kuchi achit","I'm feeling good",R.raw.phrase_im_feeling_good));
        phrases.add(new Word("әәnәs'aa?","Are you coming?",R.raw.phrase_are_you_coming));
        phrases.add(new Word("hәә'әәnәm","Yes, I'm coming",R.raw.phrase_yes_im_coming));
        phrases.add(new Word("әәnәm","I'm coming",R.raw.phrase_im_coming));
        phrases.add(new Word("yoowutis","Let's go",R.raw.phrase_lets_go));
        phrases.add(new Word("әnni'nem","Come here",R.raw.phrase_come_here));
        WordAdapter adapter=new WordAdapter(getActivity(),0,phrases,R.color.category_phrases);
        ListView list=rootView.findViewById(R.id.list);
        list.setAdapter(adapter);

        //When user clicks on listItem this OnItemClickListener is notified
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /*
                adapterView : the parent view.
                view        : The ListItem row clicked by user
                i           : ListItem number(or the position of the data is source)
                l           : ListItem ID.
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //To play the audio file we have to request for AudioFocus
                int focusRes=mAudioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                //If system has granted the audio focus then only play sound
                if(focusRes == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    //Release MediaPlayer , if it is occupied by previous listItem.
                    releaseMediaPlayer();
                    mediaPlayer=MediaPlayer.create(getActivity(),phrases.get(i).getSoundResourceId());
                    mediaPlayer.start();
                    //Listener is notified when mediaPlayer finishes the clip.
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }


            }
        });
        return rootView;
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
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
