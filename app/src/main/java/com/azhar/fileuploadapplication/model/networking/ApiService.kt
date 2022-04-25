package com.azhar.fileuploadapplication.model.networking

import com.azhar.fileuploadapplication.model.data.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("image_upload")
    suspend fun fileUpload(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): UploadResponse
}
