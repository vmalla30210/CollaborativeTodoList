class Task {
  constructor(id, title, desc, due, category) {
    this.id = id;
    this.title = title;
    this.desc = desc;
    this.due = due;
    this.category = category;
    this.done = false;
    this.user = null;
  }
  
  assign(user) {
    this.user = user;
  }
  
  complete() {
    this.done = true;
  }
  
  getInfo() {
    return `#${this.id}: ${this.title} (${this.done ? 'Done' : 'Pending'}) - ${this.category} - Assigned to: ${this.user || 'None'}`;
  }
}

module.exports = Task;
