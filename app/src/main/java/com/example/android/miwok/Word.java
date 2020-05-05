package com.example.android.miwok;
/*
    *Word class stores miwok words in miwokTranslation variable
    * and their translation into default language(English in this case) in defaultTranslation variable
 */
public class Word
{

    private String miwokTranslation,defaultTranslation;
    int imageResourceId,soundResourceId;
    public Word(String miwokWord,String defaultWord,int soundId)
    {
        miwokTranslation=miwokWord;
        defaultTranslation=defaultWord;
        imageResourceId=-1;
        soundResourceId=soundId;
    }


    public Word(String miwokWord,String defaultWord,int imageId,int sound)
    {
        miwokTranslation=miwokWord;
        defaultTranslation=defaultWord;
        imageResourceId=imageId;
        soundResourceId=sound;
    }

    public String getMiwokTranslation()
    {
        return miwokTranslation;
    }

    public String getDefaultTranslation()
    {
        return defaultTranslation;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
    public boolean hasImage()
    {
        return (imageResourceId>=0);
    }

    public int getSoundResourceId(){ return soundResourceId; }
}
