// Placeholder for app.js
// app.js
const readline = require('readline');
const TodoStore = require('./store');

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

const store = new TodoStore();
let currentUser = null;

store.on('task-added', task => {
  console.log(`Task added: #${task.id} ${task.title}`);
});

store.on('task-completed', task => {
  console.log(`Task completed: #${task.id} ${task.title}`);
});

store.on('task-deleted', task => {
  console.log(`Task deleted: #${task.id} ${task.title}`);
});

function ask(question) {
  return new Promise(resolve => {
    rl.question(question, answer => resolve(answer.trim()));
  });
}

async function login() {
  console.log("\n=== Login ===");
  
  const users = store.getAllUsers();
  if (users.length) {
    console.log(`Available users: ${users.join(', ')}`);
  }
  
  const name = await ask("Username (or new username): ");
  if (!store.getAllUsers().includes(name)) {
    await store.addUser(name);
  }
  
  currentUser = name;
  console.log(`Logged in as: ${name}`);
}

function showMenu() {
  console.log(`\n=== Menu [${currentUser}] ===`);
  console.log("1. View All Tasks");
  console.log("2. View My Tasks");
  console.log("3. Add Task");
  console.log("4. Complete Task");
  console.log("5. Delete Task");
  console.log("6. Add Category");
  console.log("7. Change User");
  console.log("8. Exit");
}

async function viewAllTasks() {
  const tasks = store.getAllTasks();
  console.log("\n=== All Tasks ===");
  
  if (!tasks.length) {
    return console.log("No tasks found");
  }
  
  tasks.forEach(task => {
    console.log(`#${task.id}: ${task.title} (${task.done ? 'Done' : 'Pending'}) - ${task.category} - Assigned to: ${task.user || 'None'}`);
  });
}

async function viewMyTasks() {
  const tasks = store.getUserTasks(currentUser);
  console.log("\n=== My Tasks ===");
  
  if (!tasks.length) {
    return console.log("No tasks assigned to you");
  }
  
  tasks.forEach(task => {
    console.log(`#${task.id}: ${task.title} (${task.done ? 'Done' : 'Pending'}) - ${task.category}`);
  });
}

async function addTask() {
  console.log("\n=== Add Task ===");
  
  const title = await ask("Title: ");
  const desc = await ask("Description: ");
  const due = await ask("Due date (yyyy-MM-dd): ");
  
  console.log(`Categories: ${store.getAllCategories().join(', ')}`);
  const category = await ask("Category: ");
  
  const assignTo = await ask("Assign to (leave empty for self): ");
  const user = assignTo || currentUser;
  
  try {
    await store.addTask(title, desc, due, category, user);
  } catch (err) {
    console.log(`Error: ${err.message}`);
  }
}

async function completeTask() {
  console.log("\n=== Complete Task ===");
  const id = parseInt(await ask("Task ID: "), 10);
  
  if (await store.completeTask(id)) {
    console.log(`Task #${id} marked as complete`);
  } else {
    console.log("Task not found");
  }
}

async function deleteTask() {
  console.log("\n=== Delete Task ===");
  const id = parseInt(await ask("Task ID: "), 10);
  
  if (await store.deleteTask(id)) {
    console.log(`Task #${id} deleted`);
  } else {
    console.log("Task not found");
  }
}

async function addCategory() {
  console.log("\n=== Add Category ===");
  const name = await ask("Category name: ");
  
  if (await store.addCategory(name)) {
    console.log(`Category added: ${name}`);
  } else {
    console.log("Failed to add category");
  }
}

async function main() {
  console.log("=== Todo List App ===");
  await store.init();
  await login();
  
  let running = true;
  while (running) {
    showMenu();
    const choice = parseInt(await ask("Choice: "), 10);
    
    switch (choice) {
      case 1: await viewAllTasks(); break;
      case 2: await viewMyTasks(); break;
      case 3: await addTask(); break;
      case 4: await completeTask(); break;
      case 5: await deleteTask(); break;
      case 6: await addCategory(); break;
      case 7: await login(); break;
      case 8: running = false; break;
      default: console.log("Invalid choice");
    }
  }
  
  rl.close();
  console.log("Goodbye!");
}

main().catch(err => {
  console.error(`Error: ${err.message}`);
  rl.close();
});