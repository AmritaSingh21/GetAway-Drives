package com.example.getawaydrives.repositories;

import com.example.getawaydrives.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,String> {

    List<Document> getDocumentsByVehicleIdAndDocumentType(Integer vehicleId, Integer docType);

}
