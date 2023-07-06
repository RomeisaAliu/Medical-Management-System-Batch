package com.example.medicalBatch.controller;

import com.example.medicalBatch.service.DoctorAppointmentService;
import com.example.medicalmanagement.dto.UserDto;
import com.example.medicalmanagement.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class AppointmentNotificationController {

    private final DoctorAppointmentService appointmentService;

    @Autowired
    public AppointmentNotificationController(DoctorAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PostMapping("/send")
    public ResponseEntity<String> sendAppointmentNotifications(@RequestBody UserDto user) {
        List<Appointment> appointments = getAppointments();
        appointmentService.sendAppointmentNotifications(user, appointments);
        return ResponseEntity.ok("Appointment notifications sent successfully.");
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendAppointmentNotifications() {
        UserDto user = getUser();
        List<Appointment> appointments = getAppointments();
        appointmentService.sendAppointmentNotifications(user, appointments);
        return ResponseEntity.ok("Appointment notifications sent successfully.");
    }

    private UserDto getUser() {
        UserDto user = new UserDto();
        user.setEmail("romeisaaliu1@gmail.com");
        return user;
    }

    private List<Appointment> getAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        return appointments;
    }
}
