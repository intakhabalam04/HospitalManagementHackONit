package com.intakhab.hospitalmanagementhackonit.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String sender;
    private String receiver;
    private String subject;
    private String message;
    private String attachmentPath;
    private Map<String, Object> model;
    private String templateName;
}
