package com.example.utaste.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.utaste.R;
import com.example.utaste.model.RecipeSalesSummary;

import java.util.List;

public class SalesReportAdapter extends ArrayAdapter<RecipeSalesSummary> {

    private final LayoutInflater inflater;

    public SalesReportAdapter(Context context, List<RecipeSalesSummary> items) {
        super(context, 0, items);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sales_summary, parent, false);
            h = new ViewHolder();
            h.name = convertView.findViewById(R.id.summaryRecipeNameText);
            h.count = convertView.findViewById(R.id.summarySalesCountText);
            h.avg = convertView.findViewById(R.id.summaryAvgRatingText);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        RecipeSalesSummary s = getItem(position);
        if (s != null) {
            h.name.setText(s.getRecipeName());
            h.count.setText("Sales: " + s.getSaleCount());
            h.avg.setText(String.format("Average rating: %.2f / 5", s.getAvgRating()));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView count;
        TextView avg;
    }
}
