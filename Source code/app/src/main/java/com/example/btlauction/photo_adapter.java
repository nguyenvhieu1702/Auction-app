package com.example.btlauction;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class photo_adapter extends RecyclerView.Adapter<photo_adapter.PhotoViewHolder> {
    Context mContext;
    ArrayList<Uri> mListPhoto;
    public photo_adapter(Context mContext){
        this.mContext = mContext;
    }

    public void setdata(ArrayList<Uri> list){
        this.mListPhoto = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri uri = mListPhoto.get(position);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            holder.imgPhoto.setImageBitmap(bitmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        if (mListPhoto == null){
            return 0;
        }else{
            return  mListPhoto.size();
        }
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPhoto;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }
    }
}
