package com.global.sarthi.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.global.sarthi.db.DataStoreManager
import com.global.sarthi.utils.Utils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope

@HiltWorker
class LocationWorker
@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dataStoreManager: DataStoreManager,
    private val utils: Utils,
    private val scope: CoroutineScope
) : Worker(context,workerParameters){
    override fun doWork(): Result {
        TODO("Not yet implemented")
    }
}