package com.alliancetechnologie.at_wallet_client.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alliancetechnologie.at_wallet_client.R;
import com.alliancetechnologie.at_wallet_client.entity.Transactions;
import com.alliancetechnologie.at_wallet_client.payload.enumerate.ETransactionType;
import com.alliancetechnologie.at_wallet_client.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.NewsViewHolder> implements Filterable {

    List<Transactions> Data = new ArrayList<>(), FullData;
    Context context;

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card_item, parent, false);
        context = parent.getContext();
        return new NewsViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int position) {
        Transactions currentTransaction = Data.get(position);

        newsViewHolder.Title.setText(currentTransaction.getTransactionName());
        newsViewHolder.Date.setText(currentTransaction.getTransactionDate());
        if (currentTransaction.getTransactionType().equals(ETransactionType.CREDIT)) {
            newsViewHolder.Amount.setText(String.format("+%s", Utils.showFormattedAmount(currentTransaction.getTransactionAmount())));
            newsViewHolder.Amount.setTextColor(context.getResources().getColor(R.color.light_green));
        } else if (currentTransaction.getTransactionType().equals(ETransactionType.DEBIT)) {
            newsViewHolder.Amount.setText(String.format("-%s", Utils.showFormattedAmount(currentTransaction.getTransactionAmount())));
            newsViewHolder.Amount.setTextColor(context.getResources().getColor(R.color.light_red));
        }
        switch (currentTransaction.getPaymentCanal()) {
            case QR_CODE:
            case CARTE:
                newsViewHolder.Icon.setImageResource(R.drawable.ic_qr_code);
                break;
            case RECHARGE:
                newsViewHolder.Icon.setImageResource(R.drawable.ic_phone);
                break;
            case ALIMENTATION:
                newsViewHolder.Icon.setImageResource(R.drawable.ic_dollar);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Transactions> list) {
        this.Data = list;
        FullData = new ArrayList<>(Data);
        notifyDataSetChanged();
    }
    public void setListWithoutNotify(List<Transactions> list) {
        this.Data = list;
        FullData = new ArrayList<>(Data);
        //notifyDataSetChanged();
    }

    public Transactions getTransactionAt(int pos) {
        return Data.get(pos);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
//            Log.e("TAG", "performFiltering: ");
            List<Transactions> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(FullData);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Transactions item : FullData) {
                    if (item.getTransactionName() != null && item.getTransactionName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Data.clear();
            Data.addAll((Collection<? extends Transactions>) results.values);
            notifyDataSetChanged();
        }
    };

    //Filter on the type of the transction
//    public Filter getFilterType() {
//        return filterType;
//    }
//    private final Filter filterType = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            Log.e("TAG", "performFiltering: ");
//            List<Transactions> filteredList = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0) {
//                filteredList.addAll(FullData);
//            } else {
//                for (Transactions item : FullData) {
//                    if (item.getTransactionType().getName() != null && item.getTransactionType().getName().equals(constraint)) {
//                        filteredList.add(item);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @SuppressLint("NotifyDataSetChanged")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            Data.clear();
//            Data.addAll((Collection<? extends Transactions>) results.values);
//            notifyDataSetChanged();
//        }
//    };


    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView Title, Amount, Date;
        ImageView Icon;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            Title = itemView.findViewById(R.id.transaction_title);
            Amount = itemView.findViewById(R.id.transaction_amount);
            Date = itemView.findViewById(R.id.trasaction_date);
            Icon = itemView.findViewById(R.id.transaction_icon);
        }
    }

}
