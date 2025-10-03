package org.example.controller;

import org.example.models.User;
import org.example.services.AuthService;

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user.
     * @param username Username
     * @param email Email address
     * @param password Password
     * @param role User role
     * @return The created user
     * @throws Exception if username or email is already in use
     */
    public User register(String username, String email, String password, String role) throws Exception {
        return authService.register(username, email, password, role);
    }

    /**
     * Authenticate a user.
     * @param usernameOrEmail Username or email
     * @param password Password
     * @return true if login successful
     */
    public boolean login(String usernameOrEmail, String password) {
        return authService.login(usernameOrEmail, password);
    }

    /**
     * Logout action.
     * In desktop apps, this usually means clearing session state or redirecting UI.
     */
    public void logout() {
        System.out.println("User logged out.");
        // Optionally: clear cached user info, reset UI, etc.
    }
}
