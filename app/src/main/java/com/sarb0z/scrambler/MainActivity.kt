package com.sarb0z.scrambler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sarb0z.scrambler.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private static final String DOWNLOAD_COMPLETE_ACTION = "android.intent.action.DOWNLOAD_COMPLETE";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Schedule the worker to run periodically
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
            MediaScramblerWorker.class,
            24, TimeUnit.HOURS)
            .build();

        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
        // Register the BroadcastReceiver to listen for DOWNLOAD_COMPLETE broadcasts
        registerDownloadCompleteReceiver();
        // TODO: Implement logic to detect when a new file is downloaded and enqueue the worker
        // For example, use a BroadcastReceiver to listen for DOWNLOAD_COMPLETE broadcasts
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
    private void registerDownloadCompleteReceiver() {
        IntentFilter intentFilter = new IntentFilter(DOWNLOAD_COMPLETE_ACTION);
        BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Handle the download complete event
                enqueueMediaOrganizerWorker();
            }
        };
        registerReceiver(downloadCompleteReceiver, intentFilter);
    }

    private void enqueueMediaOrganizerWorker() {
        // Enqueue the MediaOrganizerWorker when a new file is downloaded
        OneTimeWorkRequest downloadCompleteWorkRequest =
                new OneTimeWorkRequest.Builder(MediaOrganizerWorker.class)
                        .build();

        WorkManager.getInstance(this).enqueue(downloadCompleteWorkRequest);
    }
}