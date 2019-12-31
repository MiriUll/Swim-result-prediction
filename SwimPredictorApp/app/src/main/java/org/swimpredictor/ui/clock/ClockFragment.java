package org.swimpredictor.ui.clock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import org.swimpredictor.R;

import java.util.Objects;

import static java.lang.String.format;

/**
 * Most of the logic in this class was copied from:
 * https://www.android-examples.com/android-create-stopwatch-example-tutorial-in-android-studio/
 * Last access: 31.12.2019
 */

public class ClockFragment extends Fragment {

    private boolean timer_running;

    private long StartTime;
    private long TimeBuff;
    private long MillisecondTime;
    private long UpdateTime;

    private Handler handler;

    private int Minutes;
    private int Seconds;
    private int MilliSeconds ;

    private ImageButton startStop;

    private TextView timer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // load shared preferences
        final Context context = getActivity();
        String prefKey = getResources().getString(R.string.preference_file_key);
        SharedPreferences pref = Objects.requireNonNull(context).getSharedPreferences(prefKey, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        final View root = inflater.inflate(R.layout.fragment_clock, container, false);
        timer = root.findViewById(R.id.txtTimer);
        startStop = root.findViewById(R.id.start_stop);
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer_running){
                    //clock is running -> stop clock and store time value as 50m time
                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                    startStop.setImageResource(R.drawable.ic_play_arrow_50px);
                    timer_running = false;
                    editor.putLong("latest_stopwatch", UpdateTime);
                    editor.apply();
                } else{
                    //clock is not running -> start clock
                    StartTime = SystemClock.uptimeMillis() - UpdateTime;
                    handler.postDelayed(runnable, 0);

                    startStop.setImageResource(R.drawable.ic_pause_50px);
                    timer_running = true;
                }
            }
        });
        ImageButton reset = root.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;

                timer.setText(getResources().getString(R.string.time0));
                editor.putLong("latest_stopwatch", 0L);
                editor.commit();
                timer_running = false;
            }
        });

        handler = new Handler();

        timer_running = false;

        // load recent stopwatch result from preferences
        long latestStopwatch = pref.getLong("latest_stopwatch", 0L);
        if (latestStopwatch > 0)
            UpdateTime = latestStopwatch;
            formatTimerText();
        return root;
    }

    private Runnable runnable = new Runnable() {

        public void run() {
            long millisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + millisecondTime;

            formatTimerText();

            handler.postDelayed(this, 0);
        }

    };

    private void formatTimerText(){
        Seconds = (int) (UpdateTime / 1000);

        Minutes = Seconds / 60;

        Seconds = Seconds % 60;

        MilliSeconds = (int) (UpdateTime % 1000);

        String displayText = format("%d:%s:%s", Minutes, format("%02d", Seconds), format("%03d", MilliSeconds));
        timer.setText(displayText);
    }
}