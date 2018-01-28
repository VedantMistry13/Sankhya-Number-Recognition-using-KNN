package com.versus.sankhya;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.versus.sankhya.asynctasks.ClassifyBitmapAsyncTask;
import com.versus.sankhya.models.DataModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CanvasView.OnTouchUpListener {
    private CanvasView canvasView;
    private ImageView imageView;
    private ArrayList<DataModel> dataModelArrayList;
    private TextView textView;
    private ClassifyBitmapAsyncTask classifyBitmapAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dataModelArrayList = new ArrayList<>();
        this.prepareDataSet();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        this.canvasView = (CanvasView) findViewById(R.id.canvas);
        this.canvasView.setOnTouchUpListener(this);
        this.canvasView.init(metrics);

        Button clearCanvasButton = (Button) findViewById(R.id.clear_canvas);
        clearCanvasButton.setOnClickListener(MainActivity.this);

        this.imageView = (ImageView) findViewById(R.id.bitmap_drawn);
        this.textView = (TextView) findViewById(R.id.predicted_number);

        this.classifyBitmapAsyncTask = null;
    }

    public void prepareDataSet() {
        AssetManager assetManager = getAssets();
        String files[];
        this.dataModelArrayList.clear();
        try {
            files = assetManager.list("training_digits");
            for (String file : files) {
                InputStream inputStream = assetManager.open("training_digits/" + file);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line1024 = "";
                String line = bufferedReader.readLine();
                while (line != null) {
                    line1024 += line;
                    line = bufferedReader.readLine();
                }
                byte[] bytes = line1024.getBytes();
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] == 49) {
                        bytes[i] = 1;
                    } else {
                        bytes[i] = 0;
                    }
                }
                DataModel dataModel = new DataModel();
                dataModel.number = Integer.parseInt(file.split("_")[0]);
                dataModel.bytes = bytes;
                this.dataModelArrayList.add(dataModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_canvas:
                this.clearCanvas();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTouchUp(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) 32) / width;
        float scaleHeight = ((float) 32) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        imageView.setImageBitmap(resizedBitmap);

        int pixel;
        int B, G, R;
        byte[] bits = new byte[1024];
        try{
            for(int y = 0; y < 32; y++) {
                for(int x = 0; x < 32; x++) {
                    pixel = resizedBitmap.getPixel(x, y);

                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);

                    int color = (int)(0.299 * R + 0.587 * G + 0.114 * B);
                    if (color == 0) {
                        bits[y * 32 + x] = 1;
                    } else {
                        bits[y * 32 + x] = 0;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
        }
        this.classify(bits);
    }

    public void classify(byte[] in) {
        if (this.classifyBitmapAsyncTask != null) {
            this.classifyBitmapAsyncTask.cancel(true);
        }
        classifyBitmapAsyncTask
                = new ClassifyBitmapAsyncTask(
                    in,
                    this.dataModelArrayList,
                    this.textView
                );
        classifyBitmapAsyncTask.execute();
    }

    private void clearCanvas() {
        this.canvasView.clear();
    }
}




