package com.example.bejava;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TextView textViewArray;
    GraphView graph;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView1);
        textViewArray = (TextView) findViewById(R.id.textViewArray);
        graph = (GraphView) findViewById(R.id.graphView);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        imageView = (ImageView) findViewById(R.id.imageButton);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawGraph();
                imageView.setVisibility(View.GONE);
               // textViewArray.setVisibility(View.GONE);
            }
        });
    }


    public double SaO2(double a) {
        Log.i("saO2A", a + "");
        double rez = (1.13 - (a / 3));
        Log.i("saO2rez", rez + "");
        return rez;
    }

    private double SaCO(double a) {
        Log.i("saOA", a + "");
        double rez = ((0.86 * a) - 0.72) * 0.1;
        Log.i("saOrez", rez + "");
        return rez;
    }

    public double aVidnosh(double sumX, double sumY, double sumXX, double sumXY) {
        double rez = ((((sumX * sumY) - (200 * sumXY))) / ((sumX * sumX) - (200 * sumXX)));
        Log.i("aVidnosh", rez + "");
        return rez;
    }

    private double rOne(double sumX, double sumY, double sumXX, double sumXY, double sumYY) {
        double rez = ((sumXY - ((sumX * sumY) / 200))) / ((Math.sqrt(sumXX - ((sumX * sumX) / 200))) * (Math.sqrt(sumYY - ((sumY * sumY) / 200))));
        return rez + 1;
    }

    public int Xmax(int[][] ints) {
        int XmaxInt = 0;
        for (int i = 0; i < 200; i++) {
            if (ints[i][0] > XmaxInt) {
                XmaxInt = ints[i][0];
            }
        }
        return XmaxInt;
    }

    public int Ymax(int[][] ints) {
        int YmaxInt = 0;
        for (int i = 0; i < 200; i++) {
            if (ints[i][1] > YmaxInt) {
                YmaxInt = ints[i][1];
            }
        }
        return YmaxInt;
    }

    public double[][] XiAndYi(int[][] ints) {
        double[][] XiAndYiDoubles = new double[200][2];
        double Xmax = Xmax(ints);
        double Ymax = Ymax(ints);
        for (int i = 0; i < 200; i++) {
            XiAndYiDoubles[i][0] = (double) ints[i][0] / Xmax;
            XiAndYiDoubles[i][1] = (double) ints[i][1] / Ymax;
        }
        return XiAndYiDoubles;
    }

    public double[][] XXandXYandYY(double[][] doubles) {
        double[][] XXandXYandYYDoubles = new double[200][3];
        for (int i = 0; i < 200; i++) {
            XXandXYandYYDoubles[i][0] = doubles[i][0] * doubles[i][0];
            XXandXYandYYDoubles[i][1] = doubles[i][0] * doubles[i][1];
            XXandXYandYYDoubles[i][2] = doubles[i][1] * doubles[i][1];
        }
        return XXandXYandYYDoubles;
    }

    public double sumX(double[][] XiAndYiDoubles) {
        double rez = 0;
        for (int i = 0; i < 200; i++) {
            rez += XiAndYiDoubles[i][0];
        }
        return rez;
    }

    public double sumY(double[][] XiAndYiDoubles) {
        double rez = 0;
        for (int i = 0; i < 200; i++) {
            rez += XiAndYiDoubles[i][1];
        }
        return rez;
    }

    public double sumXX(double[][] XXandXYandYYDoubles) {
        double rez = 0;
        for (int i = 0; i < 200; i++) {
            rez += XXandXYandYYDoubles[i][0];
        }
        return rez;
    }

    public double sumXY(double[][] XXandXYandYYDoubles) {
        double rez = 0;
        for (int i = 0; i < 200; i++) {
            rez += XXandXYandYYDoubles[i][1];
        }
        return rez;
    }

    public double sumYY(double[][] XXandXYandYYDoubles) {
        double rez = 0;
        for (int i = 0; i < 200; i++) {
            rez += XXandXYandYYDoubles[i][2];
        }
        return rez;
    }

    public double DiscoverSaO2(int[][] ints) {
        double[][] XiAndYiDoubles = XiAndYi(ints);
        double[][] XXandXYandYYDoubles = XXandXYandYY(XiAndYiDoubles);
        double sumX = sumX(XiAndYiDoubles);
        double sumY = sumY(XiAndYiDoubles);
        double sumXX = sumXX(XXandXYandYYDoubles);
        double sumXY = sumXY(XXandXYandYYDoubles);
        double sumYY = sumYY(XXandXYandYYDoubles);
        double r1 = rOne(sumX, sumY, sumXX, sumXY, sumYY) - 1;
        double a = aVidnosh(sumX, sumY, sumXX, sumXY);
        Log.i("r1_test", r1 + "");
        Log.i("saO2Discover", SaO2(a) + "");
        Log.i("aSao2", a + " ");
        Log.i("r1Sao2", r1 + " ");
        Log.i("arLeSao2", XXandXYandYYDoubles.length + " ");
        if (r1 >= .99) {
            return SaO2(a);
        } else
            return 0;
    }

    public double DiscoverSaCo(int[][] ints) {
        double[][] XiAndYiDoubles = XiAndYi(ints);
        double[][] XXandXYandYYDoubles = XXandXYandYY(XiAndYiDoubles);
        double sumX = sumX(XiAndYiDoubles);
        double sumY = sumY(XiAndYiDoubles);
        double sumXX = sumXX(XXandXYandYYDoubles);
        double sumXY = sumXY(XXandXYandYYDoubles);
        double sumYY = sumYY(XXandXYandYYDoubles);
        double r1 = rOne(sumX, sumY, sumXX, sumXY, sumYY) - 1;
        double a = aVidnosh(sumX, sumY, sumXX, sumXY);
        Log.i("r1_Saco", r1 + "");
        Log.i("a1_Saco", a + "");
        Log.i("sumX1_Saco", sumX + "");
        if (r1 >= .98) {
            Log.i("saCo", SaCO(a) + "");
            return SaCO(a);
        } else return 0;
    }

    public void drawGraph() {
        int i = 0;
        int[][] intsAll = new int[30000][3];
        String data = "";
        InputStream is = this.getResources().openRawResource(R.raw.log);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (is != null) {
            try {
                while ((data = reader.readLine()) != null) {
                    intsAll[i][0] = Integer.parseInt(data.substring(0, 5));
                    intsAll[i][1] = Integer.parseInt(data.substring(6, 11));
                    intsAll[i][2] = Integer.parseInt(data.substring(12, 17));
                    i++;
                }
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double[] doublesSo2 = new double[i / 200];
        int[][] ints = new int[200][2];
        for (int j = 0; j < i / 200; j++) {
            for (int k = 0; k < 200; k++) {
                ints[k][0] = intsAll[k + j * 200][0];
                ints[k][1] = intsAll[k + j * 200][2];
            }
            doublesSo2[j] = (DiscoverSaO2(ints));
            Log.i("doublesSo2DrawGraph", doublesSo2[j] + "");
            if (doublesSo2[j] == 0) {
                if (j > 1) {
                    doublesSo2[j] = doublesSo2[j - 1];
                }
            }
            Log.i("doublesSo2DrawGraphAfter", doublesSo2[j] + "");
        }

        double[] doublesSaCO = new double[i / 200];
        int[][] ints2 = new int[200][2];
        for (int j = 0; j < i / 200; j++) {
            for (int k = 0; k < 200; k++) {
                ints2[k][0] = intsAll[k + j * 200][1];
                ints2[k][1] = intsAll[k + j * 200][2];
            }
            doublesSaCO[j] = (DiscoverSaCo(ints2));
            Log.i("doublesSaCODrawGraph", doublesSaCO[j] + "");
            if (doublesSaCO[j] == 0) {
                if (j > 1) {
                    doublesSaCO[j] = doublesSaCO[j - 1];
                }
            }
            Log.i("doublesSaCODrawGraphAfter", doublesSaCO[j] + "");
        }
        textViewArray.setText("");
        graph.setVisibility(View.VISIBLE);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();
        int size = doublesSo2.length;
        for (int i2 = 10; i2 < size; i2++) {
            DataPoint point = new DataPoint(i2, doublesSo2[i2] * 100);
            series.appendData(point, true, size);
        }
        for (int i2 = 10; i2 < size; i2++) {
            DataPoint pointF = new DataPoint(i2, doublesSaCO[i2] * 100);
            series2.appendData(pointF, true, size);
        }
        graph.getViewport().setMinX(10  );
        graph.getViewport().setMaxX(size);
        graph.getViewport().setMinY(-10);
        graph.getViewport().setMaxY(110);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setColor(Color.BLACK);
        series2.setColor(Color.RED);
        graph.addSeries(series);
        graph.addSeries(series2);
        double sred=0;
        for (int l=0;l<doublesSaCO.length;l++){
            sred+=doublesSaCO[l];
        }
        sred=sred/doublesSaCO.length;
        Log.i("sredCheck",sred+"");
        if (sred>0.10) {
            textViewArray.setText("Терміново к доктору");
        }
    }
}
