package org.swimpredictor.ui.database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import org.swimpredictor.database.*;
import androidx.room.Room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.OnScrollListener;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

import org.swimpredictor.R;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DatabaseFragment extends Fragment {

    private DatabaseViewModel databaseViewModel;
    private SampleDao sampleDao;
    private List<DataSample> samplesList;
    private TableView<String[]> tableView;

    private static final String[] headers = {"Swimmer", "50m time", "100m time", "200m time"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        databaseViewModel =
                ViewModelProviders.of(this).get(DatabaseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_database, container, false);

        AppDatabase db = Room.databaseBuilder(Objects.requireNonNull(getActivity()).getApplicationContext(), AppDatabase.class, "swimpredictor_database").allowMainThreadQueries().build();
        sampleDao = db.sampleDao();

        tableView = root.findViewById(R.id.tableView);
        samplesList = sampleDao.getAll();
        String[][] samplesArray = databaseToArray(samplesList);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(getActivity(), headers));
        tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity(), samplesArray));
        int colorEvenRows = getResources().getColor(R.color.white);
        int colorOddRows = getResources().getColor(R.color.gray);
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));
        tableView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(ListView tableDataView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(ListView tableDateView, ScrollState scrollState) {

            }
        });

        tableView.addDataLongClickListener(new DeleteOnLongClickListener());
        tableView.addDataClickListener(new ShowDetailsOnClickListener());
        tableView.setSwipeToRefreshEnabled(true);
        tableView.setSwipeToRefreshListener(new SwipeToRefreshListener() {
            @Override
            public void onRefresh(RefreshIndicator refreshIndicator) {
                refresh();
            }
        });

        return root;
    }

    private void refresh(){
        samplesList = sampleDao.getAll();
        tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity(), databaseToArray(samplesList)));
    }

    private class DeleteOnLongClickListener implements TableDataLongClickListener<String[]>{

        @Override
        public boolean onDataLongClicked(final int rowIndex, String[] clickedData) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete sample")
                    .setMessage("Do you really want to detele sample "+clickedData[0])
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sampleDao.delete(samplesList.get(rowIndex));
                            refresh();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
            return true;
        }
    }

    private class ShowDetailsOnClickListener implements TableDataClickListener<String[]>{

        @Override
        public void onDataClicked(int rowIndex, String[] clickedData) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Swimmer "+clickedData[0])
                    .setMessage(samplesList.get(rowIndex).toString())
                    .setIcon(R.drawable.swimmer_emoji)
                    .setPositiveButton(android.R.string.ok,null).show();
        }
    }

    private String[][] databaseToArray(List<DataSample> samplesList){
        String[][] samplesArray = new String[samplesList.size()][4];
        for (int i=0; i<samplesList.size(); i++){
            DataSample sample = samplesList.get(i);
            samplesArray[i] = sample.toStringArray();
        }
        return samplesArray;
    }
}