package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.security.CurrentUser;
import com.pliesveld.flashnote.security.StudentPrincipal;
import com.pliesveld.flashnote.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ResponseEntity<?> retrieveStudentNotifications(@CurrentUser StudentPrincipal studentPrincipal) {
        if(studentPrincipal == null)
        {
            return ResponseEntity.badRequest().build();
        }

        int student_id = studentPrincipal.getId();
        return ResponseEntity.ok(notificationService.retrieveStudentMessages(student_id));
    }
}
