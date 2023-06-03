package com.example.getawaydrives.repositories;

import com.example.getawaydrives.entities.Rent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentRepository extends JpaRepository<Rent,String> {



}
