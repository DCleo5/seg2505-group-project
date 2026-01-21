package com.example.utaste.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.adapter.SalesReportAdapter;
import com.example.utaste.database.SaleDao;
import com.example.utaste.model.RecipeSalesSummary;

import java.util.List;

public class SalesReportActivity extends AppCompatActivity {

    private ListView salesReportListView;
    private SaleDao saleDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        salesReportListView = findViewById(R.id.salesReportListView);
        saleDao = new SaleDao(this);

        loadSalesSummary();
    }

    private void loadSalesSummary() {
        List<RecipeSalesSummary> summaries = saleDao.getSalesSummary();

        if (summaries == null || summaries.isEmpty()) {
            Toast.makeText(this, "No sales recorded yet.", Toast.LENGTH_SHORT).show();
        }

        SalesReportAdapter adapter = new SalesReportAdapter(this, summaries);
        salesReportListView.setAdapter(adapter);

        salesReportListView.setOnItemClickListener((parent, view, position, id) -> {
            RecipeSalesSummary summary = summaries.get(position);
            android.content.Intent intent = new android.content.Intent(this, RecipeCommentsActivity.class);
            intent.putExtra("RECIPE_ID", summary.getRecipeId());
            intent.putExtra("RECIPE_NAME", summary.getRecipeName());
            startActivity(intent);
        });
    }
}
