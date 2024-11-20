package managers;

import application.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserManager {
    private List<User> users;

    public UserManager() {
        this.users = new ArrayList<>();
    }

    // Add a new user
    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            System.out.println("User " + user.getUsername() + " added successfully.");
        } else {
            System.out.println("User " + user.getUsername() + " already exists.");
        }
    }

    // Find a user by username
    public Optional<User> findUserByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    // List all users (for admin use)
    public void displayAllUsers() {
        if (users.isEmpty()) {
            System.out.println("No users in the system.");
        } else {
            System.out.println("Users in the system:");
            users.forEach(user -> System.out.println("Username: " + user.getUsername() + ", Email: " + user.getEmail() + ", Type: " + user.getUserType()));
        }
    }

    // Check if a user already exists (by username)
    public boolean userExists(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    // Get the total number of users
    public int getTotalUsers() {
        return users.size();
    }

    // Get all sellers in the system
    public List<User> getSellers() {
        List<User> sellers = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType() == User.UserType.SELLER) {
                sellers.add(user);
            }
        }
        return sellers;
    }

    // Get all buyers in the system
    public List<User> getBuyers() {
        List<User> buyers = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType() == User.UserType.BUYER) {
                buyers.add(user);
            }
        }
        return buyers;
    }
}

