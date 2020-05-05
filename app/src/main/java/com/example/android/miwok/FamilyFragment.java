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
public class FamilyFragment extends Fragment {

    MediaPlayer mediaPlayer=null;
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
                case AudioManager.AUDIOFOCUS_GAIN : if(mediaPlayer!=null) mediaPlayer.start();
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
            Toast.makeText(getActivity(),"MediaPlayer released? : "+(mediaPlayer==null),Toast.LENGTH_SHORT).show();
        }
    };



    public FamilyFragment() {
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
        View rootView=inflater.inflate(R.layout.word_list,container,false);

        mAudioManager=(AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> family_members=new ArrayList<>();
        family_members.add(new Word("әpә","father",R.drawable.family_father,R.raw.family_father));
        family_members.add(new Word("әṭa","mother",R.drawable.family_mother,R.raw.family_mother));
        family_members.add(new Word("angsi","son",R.drawable.family_son,R.raw.family_son));
        family_members.add(new Word("tune","daughter",R.drawable.family_daughter,R.raw.family_daughter));
        family_members.add(new Word("taachi","older brother",R.drawable.family_older_brother,R.raw.family_older_brother));
        family_members.add(new Word("chalitti","younger brother",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        family_members.add(new Word("teṭe","older sister",R.drawable.family_older_sister,R.raw.family_older_sister));
        family_members.add(new Word("kolliti","younger sister",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        family_members.add(new Word("ama","grandmother",R.drawable.family_grandmother,R.raw.family_grandmother));
        family_members.add(new Word("paapa","grandfather",R.drawable.family_grandfather,R.raw.family_grandfather));
        WordAdapter adapter=new WordAdapter(getActivity(),0,family_members,R.color.category_family);
        ListView list=rootView.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //To play the audio file we have to request for AudioFocus
                int focusRes=mAudioManager.requestAudioFocus(afChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                //If system has granted the audio focus then only play sound
                if(focusRes == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //Release MediaPlayer , if it is occupied by previous listItem.
                    releaseMediaPlayer();
                    mediaPlayer = MediaPlayer.create(getActivity(), family_members.get(i).getSoundResourceId());
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
