package com.example.taskmanagerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

//Task data class
data class Task(val id: Int, val description: String, var isCompleted: Boolean = false)

//Task Manager class
class TaskManager {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1

    //Adding a task to the list
    fun addTask(description: String) {
        tasks.add(Task(nextId++, description))
    }

    //Removing a task based off ID
    fun removeTask(id: Int) {
        tasks.removeAll { it.id == id }
    }

    //Checking a task off
    fun checkOffTask(id: Int) {
        val taskToCheck = tasks.find { it.id == id }
        taskToCheck?.let {
            it.isCompleted = true
        }
    }

    //Returning task list as a string
    fun getTasksText(): String {
        return if (tasks.isEmpty()) {
            "No tasks available."
        } else {
            tasks.joinToString("\n") { task ->
                val status = if (task.isCompleted) "(✓)" else "( )"
                "${task.id}. $status - ${task.description}"
            }
        }
    }
}

//User interface setup
class MainActivity : ComponentActivity() {
    private val taskManager = TaskManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TaskManagerScreen(
                        modifier = Modifier.padding(innerPadding),
                        taskManager = taskManager
                    )
                }
        }
    }
}

//Display task manager UI
@Composable
fun TaskManagerScreen(modifier: Modifier = Modifier, taskManager: TaskManager) {
    var taskDescription by remember { mutableStateOf(TextFieldValue("")) }
    var taskId by remember { mutableStateOf(TextFieldValue("")) }
    var tasksText by remember { mutableStateOf(taskManager.getTasksText()) }

    Column(modifier = modifier.padding(16.dp)) {

        // Task description input
        TextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            label = { Text("Task Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Add Task button
        Button(
            onClick = {
                if (taskDescription.text.isNotEmpty()) {
                    taskManager.addTask(taskDescription.text)
                    taskDescription = TextFieldValue("")
                    tasksText = taskManager.getTasksText()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Task ID input
        TextField(
            value = taskId,
            onValueChange = { taskId = it },
            label = { Text("Task ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Remove Task button
        Button(
            onClick = {
                taskId.text.toIntOrNull()?.let {
                    taskManager.removeTask(it)
                    tasksText = taskManager.getTasksText()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Remove Task")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Check off Task button
        Button(
            onClick = {
                taskId.text.toIntOrNull()?.let {
                    taskManager.checkOffTask(it)
                    tasksText = taskManager.getTasksText()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Check Off Task")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying task list
        Text(
            text = tasksText,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

//Previewing task manager screen
@Preview(showBackground = true)
@Composable
fun TaskManagerScreenPreview() {
    TaskManagerScreen(taskManager = TaskManager())
}
