package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.stockhawk.R.id.chart;

public class DetailActivity extends AppCompatActivity{
    @BindView(chart)
    LineChart lineChart;
    Uri stockUri;
    String symbol;
    List<Entry> entries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        symbol = getIntent().getStringExtra("symbol");
        stockUri = Contract.Quote.makeUriForStock(symbol);
        Cursor cursor = getContentResolver().query(stockUri,new String[]{Contract.Quote.COLUMN_HISTORY}, null,null,null );
        //Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
        if(cursor == null)
            return;
        if(cursor.moveToFirst()){

            String h = cursor.getString(0);
            cursor.close();
            //Log.v("Cursor Object",h);
            String[] data = h.split("\\r?\\n");

            for(String d : data){
                String[] val = d.split(",");
                //Log.v("Cursor Object",""+Float.parseFloat(val[0])+", "+Float.parseFloat(val[1]));
                entries.add(new Entry(Float.parseFloat(val[0]), Float.parseFloat(val[1])));
            }

            Collections.sort(entries, new EntryXComparator());

            Log.v("Cursor Object",entries.toString());

            LineDataSet dataSet = new LineDataSet(entries, symbol); // add entries to dataset
            dataSet.setColor(Color.RED);
            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setDrawCircles(false);
            LineData lineData = new LineData(dataSet);
            lineChart.setData(lineData);
            lineChart.setScaleEnabled(false);
            CustomMarkerView mv = new CustomMarkerView (this, R.layout.custom_marker_view_layout);
            lineChart.setMarkerView(mv);
            XAxis xAxis = lineChart.getXAxis();
            xAxis.setDrawLabels(false);
            lineChart.invalidate();
        }
    }
}
