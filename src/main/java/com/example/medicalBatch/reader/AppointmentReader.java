package com.example.medicalBatch.reader;

import com.example.medicalmanagement.model.User;
import com.example.medicalmanagement.model.UserRole;
import com.example.medicalmanagement.repository.UserRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class AppointmentReader implements ItemReader<User> {

    private final UserRepository userRepository;
    private Iterator<User> doctorIterator;

    public AppointmentReader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User read() {
        if (doctorIterator == null || !doctorIterator.hasNext()) {
            List<User> doctors = userRepository.findByRolesUserRole(UserRole.DOCTOR, Sort.by("id"));
            doctorIterator = doctors.iterator();
        }
        return doctorIterator.hasNext() ? doctorIterator.next() : null;
    }
}
