package bku.iot.demoiot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class LightActivity extends AppCompatActivity{

    private LineChart LightChart;
    private ImageView LightChartBack;
    String [] LightList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_activity);

        LightChart = findViewById(R.id.LightChart);
        LightChartBack = findViewById(R.id.LightChartBack);

        LightList = getIntent().getStringArrayExtra("LightList");

        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i< LightList.length; i++) {
            float xValue = (float) i;
            float yValue = Float.parseFloat(LightList[i]);
            Log.d("TEST",  "Light Values:" + yValue);
            entries.add(new Entry(xValue, yValue));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Light");
        dataSet.setDrawFilled(true);
        dataSet.setMode((LineDataSet.Mode.CUBIC_BEZIER));

        LineData lineData = new LineData(dataSet);

        LightChart.setData(lineData);
        LightChart.invalidate();
        LightChartBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
