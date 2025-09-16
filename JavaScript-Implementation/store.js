const fs = require('fs/promises');
const path = require('path');
const EventEmitter = require('events');
const Task = require('./task');

const DATA_FILE = path.join(__dirname, 'todo-data.json');

class TodoStore extends EventEmitter {
  constructor() {
    super();
    this.tasks = [];
    this.users = new Set();
    this.categories = new Set(['Work', 'Personal']);
    this.nextId = 1;
  }
  
  async init() {
    try {
      await this.loadData();
    } catch (err) {
      console.log('Starting with empty data');
      this.users.add('admin');
    }
  }
  
  async addUser(name) {
    if (!name || this.users.has(name)) return false;
    
    this.users.add(name);
    await this.saveData();
    this.emit('user-added', name);
    return true;
  }
  
  async addCategory(name) {
    if (!name || this.categories.has(name)) return false;
    
    this.categories.add(name);
    await this.saveData();
    this.emit('category-added', name);
    return true;
  }
  
  async addTask(title, desc, due, category, user) {
    if (!this.categories.has(category)) {
      throw new Error('Category not found');
    }
    
    if (user && !this.users.has(user)) {
      throw new Error('User not found');
    }
    
    const task = {
      id: this.nextId++,
      title,
      desc,
      due,
      category,
      user,
      done: false
    };
    
    this.tasks.push(task);
    await this.saveData();
    this.emit('task-added', task);
    return task;
  }
  
  async completeTask(id) {
    const task = this.tasks.find(t => t.id === id);
    if (!task) return false;
    
    task.done = true;
    await this.saveData();
    this.emit('task-completed', task);
    return true;
  }
  
  async deleteTask(id) {
    const index = this.tasks.findIndex(t => t.id === id);
    if (index === -1) return false;
    
    const task = this.tasks[index];
    this.tasks.splice(index, 1);
    await this.saveData();
    this.emit('task-deleted', task);
    return true;
  }
  
  getAllTasks() {
    return [...this.tasks];
  }
  
  getUserTasks(user) {
    return this.tasks.filter(t => t.user === user);
  }
  
  getAllUsers() {
    return [...this.users];
  }
  
  getAllCategories() {
    return [...this.categories];
  }
  
  async loadData() {
    const data = await fs.readFile(DATA_FILE, 'utf8');
    const parsed = JSON.parse(data);
    
    this.tasks = parsed.tasks || [];
    this.users = new Set(parsed.users || []);
    this.categories = new Set(parsed.categories || []);
    this.nextId = parsed.nextId || 1;
  }
  
  async saveData() {
    const data = {
      tasks: this.tasks,
      users: [...this.users],
      categories: [...this.categories],
      nextId: this.nextId
    };
    
    await fs.writeFile(DATA_FILE, JSON.stringify(data, null, 2));
  }
}

module.exports = TodoStore;
