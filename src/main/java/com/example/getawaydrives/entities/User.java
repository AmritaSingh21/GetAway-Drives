package com.example.getawaydrives.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty(message = "Email cannot be empty.")
    private String email;
    @NotEmpty(message = "Password cannot be empty.")
    private String password;
    @NotEmpty(message = "First Name cannot be empty.")
    private String firstName;
    @NotEmpty(message = "Last Name cannot be empty.")
    private String lastName;
    private String address;
    private String phoneNumber;
    private String dlNumber;
    private Date dlExpiryDate;
    private String dlIssuingCountry;
    private int userType;
    private int status;
    private Integer cardId;
    private Integer createdBy;
    private Date createdOn;
    private Integer lastUpdatedBy;
    private Date lastUpdatedOn;
    @Transient
    private String passwordConfirm;
    @Transient
    private String dlExpiryDateString;
    @Transient
    private MultipartFile[] file;
}