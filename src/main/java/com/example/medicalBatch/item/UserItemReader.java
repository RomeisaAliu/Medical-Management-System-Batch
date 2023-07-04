package com.example.medicalBatch.item;

import com.example.medicalmanagement.model.User;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserItemReader implements ItemReader<User> {

    private List<User> users;

    public UserItemReader(List<User> users) {
        this.users = users;
    }

    @Override
    public User read() {
        if (!users.isEmpty()) {
            return users.remove(0);
        }
        return read();
    }
}
