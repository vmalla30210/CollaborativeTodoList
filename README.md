# Collaborative To-Do List Application

This repository contains two implementations of a collaborative to-do list application - one in Java and one in JavaScript/Node.js. The application allows multiple users to create, manage, and track tasks with support for categories and assignment.

## Features

- Add, view, complete, and delete tasks
- Assign tasks to users
- Categorize tasks
- Support for multiple users
- Persistent data storage
- Concurrent access support

## Running the Java Implementation

1. Navigate to the Java-Implementation directory
2. Compile the Java files:
javac -d bin src/todoapp/*.java


3. Run the application:
java -cp bin todoapp.TodoApp



## Running the JavaScript Implementation

1. Navigate to the JavaScript-Implementation directory
2. Install dependencies (if any):
npm install


3. Run the application:
node app.js


## Implementation Comparison

This project demonstrates how the same application can be implemented in two different programming languages with different paradigms. The Java implementation leverages static typing and thread-based concurrency, while the JavaScript implementation uses asynchronous programming and events.

## Usage

Both implementations provide a command-line interface with the following options:

1. View All Tasks
2. View My Tasks
3. Add Task
4. Complete Task
5. Delete Task
6. Add Category
7. Change User
8. Exit

