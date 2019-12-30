package org.swimpredictor.ui.database;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.swimpredictor.database.*;
import androidx.room.Room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import org.swimpredictor.R;

import java.util.Arrays;
import java.util.Objects;

public class DatabaseFragment extends Fragment {

    private DatabaseViewModel databaseViewModel;
    private SampleDao sampleDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        databaseViewModel =
                ViewModelProviders.of(this).get(DatabaseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_database, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);

        AppDatabase db = Room.databaseBuilder(Objects.requireNonNull(getActivity()).getApplicationContext(), AppDatabase.class, "swimpredictor_database").allowMainThreadQueries().build();
        sampleDao = db.sampleDao();
        DataSample example = new DataSample("Example", 0, 9, 2, 36.80, 86.83, 178.53);
        sampleDao.inset(example);
        textView.setText(sampleDao.getAll().toString());

        return root;
    }
}