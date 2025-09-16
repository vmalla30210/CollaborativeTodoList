// Task.java
package todoapp;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    private static int counter = 1;
    private int id;
    private String title;
    private String desc;
    private boolean done;
    private Date due;
    private User user;
    private String category;
    
    public Task(String title, String desc, Date due, String category) {
        this.id = counter++;
        this.title = title;
        this.desc = desc;
        this.due = due;
        this.category = category;
        this.done = false;
    }
    
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDesc() { return desc; }
    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    public Date getDue() { return due; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getCategory() { return category; }
    
    @Override
    public String toString() {
        return "#" + id + ": " + title + " (" + (done ? "Done" : "Pending") + ")" +
               " - " + category + " - Assigned to: " + (user != null ? user.getName() : "None");
    }
}