/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package curiosamente.com.app.activities.main.Bar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.MainActivityBroadcastReceiver;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.model.Bar;


public class BarFragment extends Fragment {

    private final String LOG_TAG = BarFragment.class.getSimpleName();
    private ArrayList<Bar> bars = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_fragment_bar, container, false);
        ListView barListView = (ListView) rootView.findViewById(R.id.bar_list);
        ArrayAdapter<Bar> barListAdapter = new BarAdapter(getActivity(), bars);
        barListView.setAdapter(barListAdapter);

        barListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Bar bar = (Bar) adapterView.getItemAtPosition(position);
                BarManager.storeSelectedBarPreference(getActivity(), bar);
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getActivity());
                Intent intent = new Intent(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_MAINACTIVITY);
                intent.putExtra(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_RETURN_OBJECT, bar);
                intent.putExtra(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_TYPE, MainActivityBroadcastReceiver.BROADCAST_RECEIVER_TYPE_SELECTED_BAR);
                broadcaster.sendBroadcast(intent);
            }
        });
        return rootView;
    }

    public void setBars(ArrayList<Bar> bars) {
        this.bars = bars;
    }
}