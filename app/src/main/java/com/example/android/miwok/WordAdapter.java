package com.example.android.miwok;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    private int colorResourceId;

    public WordAdapter(@NonNull Context context, int resource, ArrayList<Word> words, int colorResourceId) {
        super(context, resource, words);
        this.colorResourceId = colorResourceId;

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        TextView miwok_textView = view.findViewById(R.id.miwok_textView);
        TextView default_textView = view.findViewById(R.id.default_textView);

        //Changing the background colour of textContainer(Linear Layout containing two textViews)
        view.findViewById(R.id.textContainer).setBackgroundColor(ContextCompat.getColor(getContext(), colorResourceId));


        Word word = getItem(position);        //Creating an instance of word and accessing every instance with the help of 'position' variable.
        miwok_textView.setText(word.getMiwokTranslation());
        default_textView.setText(word.getDefaultTranslation());
        ImageView imageView = view.findViewById(R.id.image);
        if (word.hasImage()) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(word.getImageResourceId());
        } else {
            Log.e("Image ID not FOUND", "getView: Image ID : " + word.getImageResourceId());
            imageView.setVisibility(View.GONE);
        }


        /*view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });*/

//        ListView lv=view.findViewById(R.id.list);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final MediaPlayer mediaPlayer=MediaPlayer.create(getContext(),R.raw.number_one);
//                mediaPlayer.start();
//            }
//        });
        return view;
    }
}
