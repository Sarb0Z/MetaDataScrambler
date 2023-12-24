package com.sarb0z.scrambler

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sarb0z.scrambler.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Schedule the worker to run periodically
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
            MediaScramblerWorker.class,
            24, TimeUnit.HOURS)
            .build();

        WorkManager.getInstance(this).enqueue(periodicWorkRequest);

        // TODO: Implement logic to detect when a new file is downloaded and enqueue the worker
        // For example, use a BroadcastReceiver to listen for DOWNLOAD_COMPLETE broadcasts
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}