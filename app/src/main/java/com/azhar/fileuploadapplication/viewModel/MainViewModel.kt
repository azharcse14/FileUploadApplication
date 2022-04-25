package com.azhar.fileuploadapplication.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azhar.fileuploadapplication.model.data.UploadResponse
import com.azhar.fileuploadapplication.model.repository.Repository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MainViewModel(private val repository: Repository) : ViewModel() {

    val uploadResponse: MutableLiveData<UploadResponse> = MutableLiveData()

    fun fileUpload(
        image: MultipartBody.Part,
        desc: RequestBody
    ) {
        viewModelScope.launch {
            repository.fileUpload(image, desc)
                .catch { e ->
                    Log.d("main", "fileUpload: ${e.message}")
                }
                .collect {
                    uploadResponse.value = it
                }
        }
    }

}