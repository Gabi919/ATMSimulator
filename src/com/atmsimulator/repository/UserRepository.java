package com.atmsimulator.repository;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final String FILE_PATH = "users.txt";
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                //
                if (data.length == 3) {
                    users.add(new User(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {

        }
        return users;
    }

    public User findById(String userId) {
        List<User> users = new ArrayList<>();
        users = findAll();
        for(User i : users) {
            if (i.getUserId().equals(userId)){
                return i;
            }
        }
        return null;
    }
}
