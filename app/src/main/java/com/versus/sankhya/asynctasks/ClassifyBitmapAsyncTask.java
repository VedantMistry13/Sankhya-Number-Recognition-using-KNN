package com.versus.sankhya.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.versus.sankhya.models.DataModel;
import com.versus.sankhya.models.IndicesAndDistance;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ClassifyBitmapAsyncTask extends AsyncTask<Void, Bitmap, Integer> {
    private byte[] in;
    private ArrayList<DataModel> dataModelArrayList;
    private WeakReference<TextView> textViewWeakReference;

    public ClassifyBitmapAsyncTask(byte[] in, ArrayList<DataModel> dataModelArrayList, TextView textView) {
        super();
        this.in = in;
        this.dataModelArrayList = dataModelArrayList;
        this.textViewWeakReference = new WeakReference<>(textView);
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int[] count = new int[10];
        IndicesAndDistance[] indicesAndDistancesArray
                = new IndicesAndDistance[dataModelArrayList.size()];

        for (int i = 0; i < this.dataModelArrayList.size(); i++) {
            DataModel dataModel = dataModelArrayList.get(i);
            int distance = 0;
            for (int j = 0; j < 1024; j++) {
                distance += Math.abs(in[j] - dataModel.bytes[j]);
            }
            indicesAndDistancesArray[i] = new IndicesAndDistance();
            indicesAndDistancesArray[i].number = dataModel.number;
            indicesAndDistancesArray[i].distance = distance;
        }

        for (int i = 0; i < dataModelArrayList.size(); i++) {
            for (int j = 0; j < dataModelArrayList.size(); j++) {
                if (indicesAndDistancesArray[i].distance < indicesAndDistancesArray[j].distance) {
                    IndicesAndDistance temp = indicesAndDistancesArray[i];
                    indicesAndDistancesArray[i] = indicesAndDistancesArray[j];
                    indicesAndDistancesArray[j] = temp;
                }
            }
        }

        for (int i = 0; i < count.length; i++) {
            count[i] = 0;
        }
        for (int i = 0; i < count.length; i++) {
            IndicesAndDistance indicesAndDistance = indicesAndDistancesArray[i];
            count[indicesAndDistance.number] += 1;
        }

        int max = count[0], maxNumber = 0;
        for (int i = 1; i < count.length; i++) {
            if (max < count[i]) {
                maxNumber = i;
                max = count[i];
            }
        }

        return maxNumber;
    }

    @Override
    protected void onPostExecute(Integer predictedNumber) {
        super.onPostExecute(predictedNumber);
        Log.d("MainActivity", predictedNumber + "");
        TextView textView = this.textViewWeakReference.get();
        if (textView != null) {
            textView.setText(predictedNumber + "");
        }
    }
}

