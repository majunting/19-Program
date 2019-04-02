package com.example.fsae.a19telemetryapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphUpdate extends Fragment {

    private final Handler mHandler = new Handler();
    private Runnable rpmTimer;
    private Runnable speedTimer;
    private Runnable tpTimer;
    private Runnable brakeTimer;
    private LineGraphSeries<DataPoint> rpmSeries;
    private LineGraphSeries<DataPoint> speedSeries;
    private LineGraphSeries<DataPoint> tpSeries;
    private LineGraphSeries<DataPoint> brakeSeries;
    private double graph1LastXValue = 0d;
    private double graph2LastXValue = 0d;
    private double graph3LastXValue = 0d;
    private double graph4LastXValue = 0d;

    private DataStorage mDataStorage;
//
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_graph, container, false);

        GraphView graph1 = (GraphView) rootView.findViewById(R.id.Graph1);
        rpmSeries = new LineGraphSeries<>();
        graph1.addSeries(rpmSeries);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(500);

        GraphView graph2 = (GraphView) rootView.findViewById(R.id.Graph2);
        speedSeries = new LineGraphSeries<>();
        graph2.addSeries(speedSeries);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(500);

        GraphView graph3 = (GraphView) rootView.findViewById(R.id.Graph3);
        tpSeries = new LineGraphSeries<>();
        graph3.addSeries(tpSeries);
        graph3.getViewport().setXAxisBoundsManual(true);
        graph3.getViewport().setMinX(0);
        graph3.getViewport().setMaxX(500);

        GraphView graph4 = (GraphView) rootView.findViewById(R.id.Graph4);
        brakeSeries = new LineGraphSeries<>();
        graph4.addSeries(brakeSeries);
        graph4.getViewport().setXAxisBoundsManual(true);
        graph4.getViewport().setMinX(0);
        graph4.getViewport().setMaxX(500);

        return rootView;
    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
//    }

    @Override
    public void onResume() {
        super.onResume();
        rpmTimer = new Runnable() {
            @Override
            public void run() {
                graph1LastXValue += 1d;
                rpmSeries.appendData(new DataPoint(graph1LastXValue, mDataStorage.getRPM()), true, 50);
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(rpmTimer, 300);

        speedTimer = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue += 1d;
                speedSeries.appendData(new DataPoint(graph2LastXValue, mDataStorage.getSpeed()), true, 50);
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(speedTimer, 300);

        tpTimer = new Runnable() {
            @Override
            public void run() {
                graph3LastXValue += 1d;
                tpSeries.appendData(new DataPoint(graph3LastXValue, mDataStorage.getThrottlePos()), true, 50);
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(tpTimer, 300);

        brakeTimer = new Runnable() {
            @Override
            public void run() {
                graph4LastXValue += 1d;
                brakeSeries.appendData(new DataPoint(graph4LastXValue, mDataStorage.getBPresF()), true, 50);
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(brakeTimer, 300);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(rpmTimer);
        mHandler.removeCallbacks(speedTimer);
        mHandler.removeCallbacks(tpTimer);
        mHandler.removeCallbacks(brakeTimer);
        super.onPause();
    }

//    private DataPoint[] generateData() {
//        int count = 30;
//        DataPoint[] values = new DataPoint[count];
//        for (int i=0; i<count; i++) {
//            double x = i;
//            double f = mRand.nextDouble()*0.15+0.3;
//            double y = Math.sin(i*f+2) + mRand.nextDouble()*0.3;
//            DataPoint v = new DataPoint(x, y);
//            values[i] = v;
//        }
//        return values;
//    }


    public void setDataStorage(DataStorage DataStorage) {
        this.mDataStorage = DataStorage;
    }
//    double mLastRandom = 2;
//    Random mRand = new Random();
//    private double getRandom() {
//        return mLastRandom += mRand.nextDouble()*0.5 - 0.25;
//    }
}
