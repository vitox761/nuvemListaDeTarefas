package com.victor.nuvem


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.victor.nuvem.api.*
import com.victor.nuvem.entities.Task
import com.victor.nuvem.dao.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class MainViewModel(application: Application) : AndroidViewModel(application){

    var fullTaskList : LiveData<List<Task>>
    private var taskDao: TaskDao = TaskRoomDatabase.getInstance(application).taskDao()
    private var job = Job()
    private val coroutineContext: CoroutineContext get() = job + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)
    private val retrofitActions : RetrofitActions

    init {
        // ROOM
        fullTaskList = taskDao.getAllTasks()
        // Retrofit actions
        retrofitActions = RetrofitInitializer.getRetrofitInstance("http://10.0.2.2:3000").create(RetrofitActions::class.java)
    }


    fun insert(task: Task) = scope.launch(Dispatchers.IO) {
        taskDao.insert(task)
    }

    fun delete(task: Task) = scope.launch(Dispatchers.IO) {
        taskDao.delete(task)
        deleteData(task)
    }

    fun clear() = scope.launch(Dispatchers.IO) {
        deleteAllData(fullTaskList.value!!)
        taskDao.deleteAll()
    }

    fun update(task : Task) = scope.launch(Dispatchers.IO){
        task.complete = !task.complete
        taskDao.update(task)
        putData(task)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

   /* private fun getData() {
        val callback = retrofitActions.getTasks()

        callback.enqueue(object : Callback<List<ModelTask>> {
            override fun onFailure(call: Call<List<ModelTask>>, t: Throwable) {
                print(t.message)
            }
            override fun onResponse(call: Call<List<ModelTask>>, response: Response<List<ModelTask>>) {
                print("Tarefas recebidas com sucesso")
            }
        })
    }*/

     fun postData(task:Task) {
        val callback = retrofitActions.postTask(task.tId,task.title,task.complete)

        callback.enqueue(object : Callback<ModelTask> {
            override fun onFailure(call: Call<ModelTask>, t: Throwable) {
                print(t.message)
            }
            override fun onResponse(call: Call<ModelTask>, response: Response<ModelTask>) {
                print("Tarefa criada com sucesso")
            }
        })

    }

    private fun putData(task:Task) {
        val callback = retrofitActions.putTask(task.tId,task.title,task.complete)

        callback.enqueue(object : Callback<ModelTask> {
            override fun onFailure(call: Call<ModelTask>, t: Throwable) {
                print(t.message)
            }
            override fun onResponse(call: Call<ModelTask>, response: Response<ModelTask>) {
                print("Tarefa atualizada com sucesso")
            }
        })

    }

    private fun deleteData(task:Task) {
        val callback = retrofitActions.deleteTask(task.tId)

        callback.enqueue(object : Callback<ModelTask> {
            override fun onFailure(call: Call<ModelTask>, t: Throwable) {
                print(t.message)
            }
            override fun onResponse(call: Call<ModelTask>, response: Response<ModelTask>) {
                print("Tarefa deletada com sucesso")
            }
        })
    }

    private fun deleteAllData(taskList: List<Task>) {
        taskList.forEach {
            deleteData(it)
        }
    }
}