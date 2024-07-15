package com.example.btlauction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidViewHolder> {
    private List<Bid> bidList;
    private Context context;

    public BidAdapter(List<Bid> bidList, Context context) {
        this.bidList = bidList;
        this.context = context;
    }

    public BidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bid_item, parent, false);
        return new BidViewHolder(view);
    }

    public void onBindViewHolder(BidViewHolder holder, int position) {
        Bid bid = bidList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String bidGet = bid.getBidPrice();
        int BidPrice = Integer.parseInt(bidGet);
        String bidpriceNumber = decimalFormat.format(BidPrice);


        holder.bidPriceTextView.setText(bidpriceNumber);
        holder.bidderNameTextView.setText(bid.getName());
        holder.timeBidTextView.setText(bid.getTime());
        holder.dayBidTextView.setText(bid.getDay());

    }

    public int maxBidPrice(){
        int maxBidPrice = 0;
        for (Bid bid : bidList) {
            String bidGet = bid.getBidPrice();
            int bidPrice = Integer.parseInt(bidGet);

            if (bidPrice > maxBidPrice) {
                maxBidPrice = bidPrice;
            }
        }

        return maxBidPrice;
    }
    @Override
    public int getItemCount() {
        return bidList.size();
    }

    public class BidViewHolder extends RecyclerView.ViewHolder {
        public TextView bidPriceTextView;
        public TextView bidderNameTextView;
        public TextView timeBidTextView;

        public TextView dayBidTextView;

        public BidViewHolder(View itemView) {
            super(itemView);
            bidPriceTextView = itemView.findViewById(R.id.txt_bidprice);
            bidderNameTextView = itemView.findViewById(R.id.txt_nameCustomer);
            timeBidTextView = itemView.findViewById(R.id.txt_timeBidCustomer);
            dayBidTextView = itemView.findViewById(R.id.txt_dayBidCustomer);
        }
    }
}
