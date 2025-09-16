package todoapp;

import java.util.List;
import java.util.Scanner;

public class TodoApp {
    private static TodoManager manager = new TodoManager();
    private static Scanner scanner = new Scanner(System.in);
    private static String currentUser = null;
    
    public static void main(String[] args) {
        System.out.println("=== Todo List App ===");
        login();
        
        boolean running = true;
        while (running) {
            printMenu();
            int choice = getInt("Choice: ");
            
            switch (choice) {
                case 1: viewAllTasks(); break;
                case 2: viewMyTasks(); break;
                case 3: addTask(); break;
                case 4: completeTask(); break;
                case 5: deleteTask(); break;
                case 6: addCategory(); break;
                case 7: changeUser(); break;
                case 8: running = false; break;
                default: System.out.println("Invalid choice");
            }
        }
    }
    
    private static void login() {
        System.out.println("\n=== Login ===");
        
        System.out.println("Available users: " + manager.getUserNames());
        System.out.print("Username (or new username): ");
        currentUser = scanner.nextLine().trim();
        
        manager.getOrCreateUser(currentUser);
        System.out.println("Logged in as: " + currentUser);
    }
    
    private static void printMenu() {
        System.out.println("\n=== Menu [" + currentUser + "] ===");
        System.out.println("1. View All Tasks");
        System.out.println("2. View My Tasks");
        System.out.println("3. Add Task");
        System.out.println("4. Complete Task");
        System.out.println("5. Delete Task");
        System.out.println("6. Add Category");
        System.out.println("7. Change User");
        System.out.println("8. Exit");
    }
    
    private static void viewAllTasks() {
        List<Task> tasks = manager.getAllTasks();
        System.out.println("\n=== All Tasks ===");
        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
            return;
        }
        
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
    
    private static void viewMyTasks() {
        List<Task> tasks = manager.getUserTasks(currentUser);
        System.out.println("\n=== My Tasks ===");
        if (tasks.isEmpty()) {
            System.out.println("No tasks assigned to you");
            return;
        }
        
        for (Task task : tasks) {
            System.out.println(task);
        }
    }
    
    private static void addTask() {
        System.out.println("\n=== Add Task ===");
        
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        
        System.out.print("Description: ");
        String desc = scanner.nextLine().trim();
        
        System.out.print("Due date (yyyy-MM-dd): ");
        String due = scanner.nextLine().trim();
        
        System.out.println("Categories: " + manager.getCategories());
        System.out.print("Category: ");
        String category = scanner.nextLine().trim();
        
        System.out.print("Assign to (leave empty for self): ");
        String user = scanner.nextLine().trim();
        if (user.isEmpty()) user = currentUser;
        
        Task task = manager.addTask(title, desc, due, category, user);
        if (task != null) {
            System.out.println("Task added: " + task);
        }
    }
    
    private static void completeTask() {
        System.out.println("\n=== Complete Task ===");
        int id = getInt("Task ID: ");
        
        if (manager.completeTask(id)) {
            System.out.println("Task #" + id + " marked as complete");
        } else {
            System.out.println("Task not found");
        }
    }
    
    private static void deleteTask() {
        System.out.println("\n=== Delete Task ===");
        int id = getInt("Task ID: ");
        
        if (manager.deleteTask(id)) {
            System.out.println("Task #" + id + " deleted");
        } else {
            System.out.println("Task not found");
        }
    }
    
    private static void addCategory() {
        System.out.println("\n=== Add Category ===");
        System.out.print("Category name: ");
        String name = scanner.nextLine().trim();
        
        if (manager.addCategory(name)) {
            System.out.println("Category added: " + name);
        } else {
            System.out.println("Failed to add category");
        }
    }
    
    private static void changeUser() {
        login();
    }
    
    private static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number");
            }
        }
    }

}
