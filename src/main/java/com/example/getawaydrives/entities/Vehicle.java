package com.example.getawaydrives.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer ownerId;
    private String licensePlateNumber;
    private String build; //make
    private String model;
    private Integer year;
    private String city;
    private String province;
    private Double price;
    private String fuelConsumption;
    private String vin;
    private Integer longestTripDur;
    private Integer shortestTripDur;
    private String transmissionType;
    private String type;
    private Integer odometer;
    private Integer status;
    private Integer createdBy;
    private Date createdOn;
    private Integer lastUpdatedBy;
    private Date lastUpdatedOn;
    @Transient
    private MultipartFile[] pics;
    @Transient
    private MultipartFile[] docs;
    @OneToMany
    private List<Rent> rents;
}
