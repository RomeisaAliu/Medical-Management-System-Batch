package com.example.medicalBatch.config;

import com.example.medicalmanagement.model.User;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<User,User> {

    @Override
    public User process(User user){
        return user;
    }
}