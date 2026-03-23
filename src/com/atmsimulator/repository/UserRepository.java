package com.atmsimulator.repository;

import com.atmsimulator.model.Account;
import com.atmsimulator.model.User;

import java.io.*;
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


    private void saveAll(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User u : users) {
                String line = u.getUserId() + "," +
                        u.getFullName() + "," +
                        u.getRole();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Eroare la salvarea utilizatorilor: " + e.getMessage());
        }
    }


    public void save(User newUser) {

        List<User> allUsers = findAll();

        allUsers.add(newUser);

        saveAll(allUsers);
    }
}
