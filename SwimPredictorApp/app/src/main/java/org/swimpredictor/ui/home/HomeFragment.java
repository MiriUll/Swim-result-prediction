package org.swimpredictor.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import org.swimpredictor.R;
import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private Interpreter model100m;

    private TextView time100m;
    private TextView time_50m;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        time100m = root.findViewById(R.id.random_textview);
        time_50m = root.findViewById(R.id.time_50m);
        // load latest stopwatch result if there is one
        final Context context = getActivity();
        String prefKey = getResources().getString(R.string.preference_file_key);
        SharedPreferences pref = Objects.requireNonNull(context).getSharedPreferences(prefKey, Context.MODE_PRIVATE);
        long latestStopwatch = pref.getLong("latest_stopwatch", 0L);
        if (latestStopwatch > 0)
                time_50m.setText(toHumanReadbleTime(latestStopwatch));

        loadModelFromAssets();

        return root;
    }

    public static String toHumanReadbleTime(long time){
        int seconds = (int) (time / 1000);

        int minutes = seconds / 60;

        seconds = seconds % 60;

        int milliSeconds = (int) (time % 1000);

        return "" + minutes + ":"
                + String.format("%02d", seconds) + ","
                + String.format("%02d", (milliSeconds / 10));
    }

    private float toCommonNumberScheme(String input){
        if (input.contains(":")) {
            // uses common swim time notation
            int minutes = Integer.parseInt(input.substring(0, input.indexOf(':')));
            int seconds = Integer.parseInt(input.substring(input.indexOf(':'), input.indexOf(',')));
            int miliseconds = Integer.parseInt(input.substring(input.indexOf(','), input.length()-1));
            return minutes * 60 + seconds + (float) miliseconds / 100;
        } else{
            return Float.parseFloat(input);
        }
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
        input[0][3] = toCommonNumberScheme(time_50m.getText().toString());
        float[][] prediction = new float[1][1];
        model100m.run(input, prediction);
        time100m.setText(String.format("%s", prediction[0][0]));
    }
}