package com.example.composetest.ui.test

import android.util.Log
import com.example.composetest.data.SampleRequest
import com.example.composetest.data.SampleResponse
import com.example.composetest.data.SampleServiceGrpc
import com.example.composetest.data.SampleServiceGrpcKt
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GrpcTest {
    companion object {
        private const val ADDRESS = "URL"
    }

    private val _id = MutableSharedFlow<SampleRequest>()
    val id = _id.asSharedFlow()

    private var managedChannel: ManagedChannel? = null

    @Synchronized
    private fun getManagedChannel(): ManagedChannel? {
        if (managedChannel == null) {
            managedChannel = ManagedChannelBuilder
                .forAddress(ADDRESS, 8080)
//          .useTransportSecurity() //HTTPS인 경우 사용
                .usePlaintext()
                .build()
        }
        return managedChannel
    }

    private suspend fun getProfileTest() = coroutineScope {
        getManagedChannel()?.let { channel ->

            val stub = SampleServiceGrpcKt.SampleServiceCoroutineStub(channel)
            val requestMessage = stub.getClientStream(id).message
            Log.i("TAG", "Test() - message: ${requestMessage}" )
        }
    }
}