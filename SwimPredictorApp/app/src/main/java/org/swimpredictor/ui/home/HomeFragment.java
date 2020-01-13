package org.swimpredictor.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import org.swimpredictor.R;
import org.swimpredictor.database.AppDatabase;
import org.swimpredictor.database.DataSample;
import org.swimpredictor.database.SampleDao;
import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private Interpreter model100m;
    private Interpreter model200m;

    private int gender;
    private int age;
    private int training_age;

    private TextView time100m;
    private TextView time200m;
    private TextView time_50m;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        time100m = root.findViewById(R.id.textview_100m_time);
        time200m = root.findViewById(R.id.textview_200m_time);
        time_50m = root.findViewById(R.id.time_50m);
        // load latest stopwatch result if there is one
        final Context context = getActivity();
        String prefKey = getResources().getString(R.string.preference_file_key);
        SharedPreferences pref = Objects.requireNonNull(context).getSharedPreferences(prefKey, Context.MODE_PRIVATE);
        long latestStopwatch = pref.getLong("latest_stopwatch", 0L);
        if (latestStopwatch > 0)
                time_50m.setText(toHumanReadbleTime(latestStopwatch));

        model100m = loadModelFromAssets("model100m.tflite");
        model200m = loadModelFromAssets("model200m.tflite");

        final Button predict = root.findViewById(R.id.predict);
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predict(view);
            }
        });

        Button store = root.findViewById(R.id.store_record);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predict(view);
                final EditText inputEditTextField = new EditText(getActivity());
                inputEditTextField.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccentBlue)));
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Enter description")
                        .setMessage("Please enter a unique description to store this record.")
                        .setView(inputEditTextField)
                        .setIcon(R.drawable.swimmer_emoji)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                AppDatabase db = Room.databaseBuilder(Objects.requireNonNull(getActivity()).getApplicationContext(), AppDatabase.class, "swimpredictor_database").allowMainThreadQueries().build();
                                SampleDao sampleDao = db.sampleDao();

                                String description = inputEditTextField.getText().toString();

                                DataSample newSample = new DataSample(description, gender, age,
                                        training_age,
                                        toCommonNumberScheme(time_50m.getText().toString()),
                                        toCommonNumberScheme(time100m.getText().toString()),
                                        toCommonNumberScheme(time200m.getText().toString()));
                                sampleDao.inset(newSample);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });

        return root;
    }

    public void predict(View view){
        float[][] input = new float[1][4];
        // read the values from input
        String genderStr = ((Spinner) getView().findViewById(R.id.gender)).getSelectedItem().toString();
        gender = (int) (genderStr.equals("female") ? 1:0);
        input[0][0] = gender;
        age = Integer.parseInt(((Spinner) getView().findViewById(R.id.age)).getSelectedItem().toString());
        input[0][1] = age;
        training_age = Integer.parseInt(((Spinner) getView().findViewById(R.id.training_age)).getSelectedItem().toString());
        input[0][2] = training_age;
        //input[0][3] = (float) 36.80;
        if(time_50m.getText().toString().equals("")){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Error")
                    .setMessage("Please enter the 50m time!")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(android.R.string.ok,null).show();
            return;
        }
        input[0][3] = toCommonNumberScheme(time_50m.getText().toString());
        float[][] prediction100 = new float[1][1];
        model100m.run(input, prediction100);
        System.out.println("100m prediction: " + prediction100[0][0]);
        time100m.setText(String.format("%s", toHumanReadbleTime((long) (prediction100[0][0]*1000.0))));
        float[][] prediction200 = new float[1][1];
        model200m.run(input, prediction200);
        System.out.println("200m prediction: " + prediction200[0][0]);
        time200m.setText(String.format("%s", toHumanReadbleTime((long) (prediction200[0][0]*1000.0))));
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
        if (input.equals("--:--,--"))
                return 0F;
        if (input.contains(":")) {
            // uses common swim time notation
            int minutes = Integer.parseInt(input.substring(0, input.indexOf(':')));
            int seconds = Integer.parseInt(input.substring(input.indexOf(':')+1, input.indexOf(',')));
            int miliseconds = Integer.parseInt(input.substring(input.indexOf(',')+1));
            return minutes * 60 + seconds + (float) miliseconds / 100;
        } else{
            return Float.parseFloat(input);
        }
    }

    private Interpreter loadModelFromAssets(String model){
        File f = new File(getActivity().getCacheDir()+"/"+model);
        if (!f.exists()) try {

            InputStream is = getActivity().getAssets().open(model);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();


            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) { throw new RuntimeException(e); }
        return new Interpreter(f);
    }
}