package org.swimpredictor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
        loadModelFromAssets();
        setAdapterForSpinner(R.id.gender, getResources().getIdentifier("gender_spinner", "array", getPackageName()));
        setAdapterForSpinner(R.id.age, getResources().getIdentifier("age_spinner", "array", getPackageName()));
        setAdapterForSpinner(R.id.training_age, getResources().getIdentifier("training_age_spinner", "array", getPackageName()));

    }

    private void setAdapterForSpinner(int spinnerID, int ressource){
        Spinner spinner = (Spinner) findViewById(spinnerID);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), ressource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadModelFromAssets(){
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
        // read the values from input
        String gender = ((Spinner) findViewById(R.id.gender)).getSelectedItem().toString();
        input[0][0] = (float) (gender.equals("female") ? 1:0);
        input[0][1] = Float.parseFloat(((Spinner) findViewById(R.id.age)).getSelectedItem().toString());
        input[0][2] = Float.parseFloat(((Spinner) findViewById(R.id.training_age)).getSelectedItem().toString());
        //input[0][3] = (float) 36.80;
        input[0][3] = Float.parseFloat(((EditText) findViewById(R.id.time_50m)).getText().toString());
        float[][] prediction = new float[1][1];
        model100m.run(input, prediction);
        TextView text = (TextView) findViewById(R.id.random_textview);
        text.setText(prediction[0][0] + "");
    }
}
