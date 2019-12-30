package org.swimpredictor.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import org.swimpredictor.R;
import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    protected Interpreter model100m;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        loadModelFromAssets();

        return root;
    }



    private void loadModelFromAssets(){
        File f = new File(getActivity().getCacheDir()+"/model100m.tflite");
        if (!f.exists()) try {

            InputStream is = getActivity().getAssets().open("model100m.tflite");
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
        String gender = ((Spinner) getView().findViewById(R.id.gender)).getSelectedItem().toString();
        input[0][0] = (float) (gender.equals("female") ? 1:0);
        input[0][1] = Float.parseFloat(((Spinner) getView().findViewById(R.id.age)).getSelectedItem().toString());
        input[0][2] = Float.parseFloat(((Spinner) getView().findViewById(R.id.training_age)).getSelectedItem().toString());
        //input[0][3] = (float) 36.80;
        input[0][3] = Float.parseFloat(((EditText) getView().findViewById(R.id.time_50m)).getText().toString());
        float[][] prediction = new float[1][1];
        model100m.run(input, prediction);
        TextView text = (TextView) getView().findViewById(R.id.random_textview);
        text.setText(prediction[0][0] + "");
    }
}