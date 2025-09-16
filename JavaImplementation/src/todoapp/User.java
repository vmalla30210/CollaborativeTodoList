package todoapp;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<Task> tasks = new ArrayList<>();
    
    public User(String name) {
        this.name = name;
    }
    
    public String getName() { return name; }
    public List<Task> getTasks() { return tasks; }
    public void addTask(Task task) { tasks.add(task); }
    public void removeTask(Task task) { tasks.remove(task); }
    
    @Override
    public String toString() { return name; }

}
