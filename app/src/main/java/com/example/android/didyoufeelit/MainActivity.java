/*
 * Copyright (C) 2016 The Android Open Source Project
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
package com.example.android.didyoufeelit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Displays the perceived strength of a single earthquake event based on responses from people who
 * felt the earthquake.
 */
public class MainActivity extends AppCompatActivity {

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Create an object of our AsyncTask class and then executes it on the background thread
         * sending our request url as a parameter
         */
        EventAsync eventAsync = new EventAsync();
        eventAsync.execute(USGS_REQUEST_URL);
    }

    /**
     * Update the UI with the given earthquake information.
     */
    private void updateUi(Event earthquake) {
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(earthquake.title);

        TextView tsunamiTextView = (TextView) findViewById(R.id.number_of_people);
        tsunamiTextView.setText(getString(R.string.num_people_felt_it, earthquake.numOfPeople));

        TextView magnitudeTextView = (TextView) findViewById(R.id.perceived_magnitude);
        magnitudeTextView.setText(earthquake.perceivedStrength);
    }

    /**
     * Custom class that extends {@link AsyncTask} in order to call the networking request
     * on the worker thread instead of the main thread
     */
    private class EventAsync extends AsyncTask<String, Void, Event>{

        /**
         * Our class will process the Http request operation in this method.
         * @param strings the method receives a string[] as parameter, which is typically our
         *                string url
         * @return the method will return our event object containing the data fetched from
         * the http response
         */
        @Override
        protected Event doInBackground(String... strings) {
            return Utils.fetchEarthquakeData(strings[0]);
        }

        /**
         * after the background worker finishes fetching the data from the internet, the event
         * object will be captured by this method post execution of the worker thread
         * @param event the event object received as a result of the doInBackground method
         */
        @Override
        protected void onPostExecute(Event event) {
            //Update the ui with our data
            updateUi(event);
        }
    }


}
