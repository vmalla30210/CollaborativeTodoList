// TodoManager.java
package todoapp;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TodoManager {
    private List<Task> tasks = Collections.synchronizedList(new ArrayList<>());
    private Map<String, User> users = new ConcurrentHashMap<>();
    private Set<String> categories = ConcurrentHashMap.newKeySet();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final String DATA_FILE = "todo_data.ser";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public TodoManager() {
        loadData();
        if (categories.isEmpty()) {
            categories.add("Work");
            categories.add("Personal");
        }
        if (users.isEmpty()) {
            users.put("admin", new User("admin"));
        }
    }
    
    public User getOrCreateUser(String name) {
        return users.computeIfAbsent(name, User::new);
    }
    
    public boolean addCategory(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return categories.add(name.trim());
    }
    
    public Task addTask(String title, String desc, String dueStr, String category, String userName) {
        lock.writeLock().lock();
        try {
            Date due = null;
            try {
                due = DATE_FORMAT.parse(dueStr);
            } catch (Exception e) {
                System.out.println("Invalid date format. Use yyyy-MM-dd");
                return null;
            }
            
            if (!categories.contains(category)) {
                System.out.println("Category not found");
                return null;
            }
            
            Task task = new Task(title, desc, due, category);
            
            if (userName != null && !userName.isEmpty()) {
                User user = getOrCreateUser(userName);
                task.setUser(user);
                user.addTask(task);
            }
            
            tasks.add(task);
            saveData();
            return task;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean completeTask(int id) {
        lock.writeLock().lock();
        try {
            for (Task task : tasks) {
                if (task.getId() == id) {
                    task.setDone(true);
                    saveData();
                    return true;
                }
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean deleteTask(int id) {
        lock.writeLock().lock();
        try {
            Iterator<Task> it = tasks.iterator();
            while (it.hasNext()) {
                Task task = it.next();
                if (task.getId() == id) {
                    it.remove();
                    if (task.getUser() != null) {
                        task.getUser().removeTask(task);
                    }
                    saveData();
                    return true;
                }
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public List<Task> getAllTasks() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(tasks);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public List<Task> getUserTasks(String userName) {
        lock.readLock().lock();
        try {
            User user = users.get(userName);
            return user != null ? new ArrayList<>(user.getTasks()) : new ArrayList<>();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public Set<String> getCategories() {
        return new HashSet<>(categories);
    }
    
    public Set<String> getUserNames() {
        return new HashSet<>(users.keySet());
    }
    
    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(tasks);
            out.writeObject(new ArrayList<>(categories));
            
            List<String> userNames = new ArrayList<>(users.keySet());
            out.writeObject(userNames);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            tasks = (List<Task>) in.readObject();
            categories = new HashSet<>((List<String>) in.readObject());
            
            List<String> userNames = (List<String>) in.readObject();
            for (String name : userNames) {
                users.put(name, new User(name));
            }
            
            for (Task task : tasks) {
                if (task.getUser() != null) {
                    String userName = task.getUser().getName();
                    User user = users.get(userName);
                    if (user != null) {
                        task.setUser(user);
                        user.addTask(task);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}