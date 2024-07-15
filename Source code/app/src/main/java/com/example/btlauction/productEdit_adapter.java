package com.example.btlauction;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.util.List;

public class productEdit_adapter extends RecyclerView.Adapter<productEdit_adapter.PhotoViewHolder> {
    Context mContext;
    List<Product> mProduct;
    private LayoutInflater mLayoutInflater;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public productEdit_adapter( List<Product> datas,Context mContext){
        this.mProduct = datas;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_editpd, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Product product = mProduct.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String priceGet = product.getPrice();
        int Price = Integer.parseInt(priceGet);
        String priceNumber = decimalFormat.format(Price);

        String bidGet = product.getBidPrice();
        int bidPrice = Integer.parseInt(bidGet);
        String bidPriceNumber = decimalFormat.format(bidPrice);


        holder.textView1.setText(product.getName());
        holder.textView2.setText("Giá tiền hiện tại: "+priceNumber);
        holder.textView4.setText(product.getTime());
        List<String> imageUrls = product.getImageUrls();
        if (!imageUrls.isEmpty()) {
            Glide.with(mContext)
                    .load(imageUrls.get(0)) // chọn phần tử đầu tiên trong danh sách
                    .into(holder.imgPhoto);
        }
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivityEditAuction.class);
                intent.putExtra("id",product.getIDpd());
                intent.putExtra("image",imageUrls.get(0));
                intent.putExtra("name",product.getName());
                intent.putExtra("describe",product.getDescribe());
                intent.putExtra("bidPrice",product.getBidPrice());
                intent.putExtra("price",product.getPrice());

                intent.putExtra("dayStart",product.getDayStart());
                intent.putExtra("monthStart",product.getMonthStart());
                intent.putExtra("yearStart",product.getYearStart());
                intent.putExtra("hourStart",product.getHourStart());
                intent.putExtra("minuteStart",product.getMinuteStart());

                intent.putExtra("dayEnd",product.getDayEnd());
                intent.putExtra("monthEnd",product.getMonthEnd());
                intent.putExtra("yearEnd",product.getYearEnd());
                intent.putExtra("hourEnd",product.getHourEnd());
                intent.putExtra("minuteEnd",product.getMinuteEnd());



                intent.putExtra("studio",product.getNameStudio());
                intent.putExtra("ratio",product.getRatio());
                intent.putExtra("material",product.getMaterial());
                intent.putExtra("condition",product.getCondition());
                intent.putExtra("weight",product.getWeight());
                intent.putExtra("dimension",product.getDimension());
                mContext.startActivity(intent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return mProduct.size();
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        ImageView imgPhoto;
        Button btn;
        TextView textView1,textView2,textView3,textView4;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photoMyAuction);
            btn = itemView.findViewById(R.id.button_MyAuction);
            textView1 = itemView.findViewById(R.id.txt_nameMyAuction);
            textView2 = itemView.findViewById(R.id.txt_priceMyAuction);
            textView4 = itemView.findViewById(R.id.txt_timeMyAuction);
        }
    }
}

