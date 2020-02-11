package com.victor.nuvem.api
import com.google.gson.annotations.SerializedName

data class ModelTask(
    @SerializedName("id")
    var id : Int,
    @SerializedName("task_title")
    var task_title : String,
    @SerializedName("complete")
    var complete : Boolean
)