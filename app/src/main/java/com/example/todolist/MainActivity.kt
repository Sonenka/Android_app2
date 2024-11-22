package com.example.todolist

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextTask: EditText = findViewById(R.id.editTextTask)
        val buttonAddTask: Button = findViewById(R.id.buttonAddTask)
        val recyclerViewTasks: RecyclerView = findViewById(R.id.recyclerViewTasks)

        // Инициализация адаптера и RecyclerView
        taskAdapter = TaskAdapter(
            tasks = mutableListOf(),
            onDeleteClick = { task ->
                taskAdapter.removeTask(task)
            },
            onEditClick = { task, position ->
                showEditDialog(task, position)
            }
        )

        recyclerViewTasks.adapter = taskAdapter
        recyclerViewTasks.layoutManager = LinearLayoutManager(this)

        // Событие добавления новой задачи
        buttonAddTask.setOnClickListener {
            val taskName = editTextTask.text.toString()
            if (taskName.isNotBlank()) {
                val task = Task(taskName)
                taskAdapter.addTask(task)
                editTextTask.text.clear()
            }
        }
    }

    private fun showEditDialog(task: Task, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_task, null)
        val editTextTaskName = dialogView.findViewById<EditText>(R.id.editTextTaskName)

        editTextTaskName.setText(task.name)

        AlertDialog.Builder(this)
            .setTitle("Редактировать задачу")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newTaskName = editTextTaskName.text.toString()
                if (newTaskName.isNotBlank()) {
                    val updatedTask = task.copy(name = newTaskName)
                    taskAdapter.updateTask(position, updatedTask)
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
