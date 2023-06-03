package com.example.getawaydrives.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer ownerId;
    //private Integer vehicleId;
    private Integer renterId;
    private Date startDate;
    private Date endDate;
    private Date returnDate;
    private Double price;
    private Double deposit;
    private Double fine;
    private Integer status;
    private Integer createdBy;
    private Date createdOn;
    private Integer lastUpdatedBy;
    private Date lastUpdatedOn;
    @ManyToOne
    private Vehicle vehicle;
}
