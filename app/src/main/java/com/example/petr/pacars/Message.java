package com.example.petr.pacars;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Message {
    private String name;
    private String fullText;
    private String text;
    private String url;

    public Message(String name, String text, String url, String fullText){
        this.name=name;
        this.text = text;
        this.url = url;
        this.fullText=fullText;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void getImage(Context context, ImageView view)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference mountainImagesRef = storageRef.child(url);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(mountainImagesRef).into(view);
    }

    public void setImage(String url) {
        this.url = url;
    }

    public String getFullText() {
        return this.fullText;
    }
}
