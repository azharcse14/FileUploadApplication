package com.azhar.fileuploadapplication.model.repository

import com.azhar.fileuploadapplication.model.data.UploadResponse
import com.azhar.fileuploadapplication.model.networking.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository {

    fun fileUpload(
        image: MultipartBody.Part,
        desc: RequestBody
    ): Flow<UploadResponse> = flow {
        val data = RetrofitBuilder.API_SERVICE.fileUpload(image, desc)
        emit(data)
    }.flowOn(Dispatchers.IO)
}