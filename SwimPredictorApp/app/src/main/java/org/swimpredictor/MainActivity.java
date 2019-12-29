package org.swimpredictor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    protected Interpreter model100m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File f = new File(getCacheDir()+"/model100m.tflite");
        if (!f.exists()) try {

            InputStream is = getAssets().open("model100m.tflite");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) { throw new RuntimeException(e); }
        model100m = new Interpreter(f);
    }

    public void predictSomeResults(View view){
        //double prediction = 0.0;
        //double[] input =  {0, 9, 2, 36.80};
        //print(model100m.getInputDetails());
        float[][] input = new float[1][4];
        input[0][0] = (float) 0;
        input[0][1] = (float) 9;
        input[0][2] = (float) 2;
        input[0][3] = (float) 36.80;
        System.out.println(input[0].length + "");
        float[][] prediction = new float[1][1];
        model100m.run(input, prediction);
        TextView text = (TextView) findViewById(R.id.random_textview);
        text.setText(Arrays.toString(prediction[0]));
    }
}
