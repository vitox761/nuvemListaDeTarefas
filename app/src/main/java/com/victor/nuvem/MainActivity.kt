package com.victor.nuvem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.victor.nuvem.entities.Task
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var taskList = emptyList<Task>()
    private lateinit var mainViewModel: MainViewModel
    private var itemList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter =ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_multiple_choice
            , itemList)
        listView.adapter = adapter

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Criando observer para LiveData
        mainViewModel.fullTaskList.observe(this, Observer { fullTaskList ->
            fullTaskList?.let{

                // Populando taskList
                taskList = fullTaskList

                // Adicionando novo item na API e UI
                if(taskList.size > itemList.size){
                    mainViewModel.postData(taskList.last())
                    itemList.add(taskList.last().title)
                }

                // Populando lista caso esteja vazia
                if(itemList.isEmpty()){
                    taskList.forEach {
                        itemList.add(it.title)
                        // CHECKBOX STATUS
                        listView.setItemChecked(itemList.size - 1 ,it.complete)
                    }
                }

                // Limpar lista
                if(taskList.isEmpty()){
                    itemList.clear()
                }

                // UPDATE UI
                adapter.notifyDataSetChanged()
            }
        })

        // Adionar nova tarefa ao apertar botao add
        add.setOnClickListener {
            // Criando Tarefa
            val newTask = Task()
            newTask.title = editText.text.toString()

            // Inserindo em Room
            mainViewModel.insert(newTask)

            // Limpando textInput
            editText.text.clear()
        }

        // Limpar lista ao apertar botao limpar
        clear.setOnClickListener {
            // Limpando Room e API
            mainViewModel.clear()
        }

        // Alterando o booleano ao tocar na celula
        listView.setOnItemClickListener { _, _, i, _ ->
            // UPDATE ROOM E API
            mainViewModel.update(taskList[i])
        }

        // Deletando itens selecionados
        delete.setOnClickListener {
            val position: SparseBooleanArray = listView.checkedItemPositions
            val count = listView.count
            var item = count + 1
            while (item >= 0) {
                if (position.get(item))
                {
                    // Deletando tarefa da Room e API
                    mainViewModel.delete(taskList[item])
                }
                item--
            }
            position.clear()
        }
    }
}
