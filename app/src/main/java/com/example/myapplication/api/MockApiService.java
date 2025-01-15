package com.example.myapplication.api;

import android.os.Handler;
import android.os.Looper;
import com.example.myapplication.models.User;
import java.util.HashMap;
import java.util.Map;

public class MockApiService {
    private static MockApiService instance;
    private final Map<String, User> users = new HashMap<>();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private MockApiService() {
        users.put("test@example.com", new User("test@example.com", "password123", "Test User", null));
    }

    public static synchronized MockApiService getInstance() {
        if (instance == null) {
            instance = new MockApiService();
        }
        return instance;
    }

    public interface ApiCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public void login(String email, String password, ApiCallback callback) {
        handler.postDelayed(() -> {
            User user = users.get(email);
            if (user != null && user.getPassword().equals(password)) {
                callback.onSuccess(user);
            } else {
                callback.onError("Invalid email or password");
            }
        }, 1000);
    }

    public void signup(String email, String password, String name, ApiCallback callback) {
        handler.postDelayed(() -> {
            if (users.containsKey(email)) {
                callback.onError("Email already exists");
                return;
            }

            User newUser = new User(email, password, name, null);
            users.put(email, newUser);
            callback.onSuccess(newUser);
        }, 1000);
    }
}