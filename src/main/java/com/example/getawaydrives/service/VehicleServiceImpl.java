package com.example.getawaydrives.service;

import com.example.getawaydrives.entities.Rent;
import com.example.getawaydrives.entities.RentStatus;
import com.example.getawaydrives.entities.Vehicle;
import com.example.getawaydrives.repositories.VehicleRepository;
import com.example.getawaydrives.utility.CommonMethods;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class VehicleServiceImpl {
    public static Vehicle addVehicle(Vehicle vehicle, VehicleRepository repository, Integer userId) {
        try {
            if (checkIfVehicleExists(vehicle.getVin(), repository)) {
                throw new Exception("Vehicle already exists!");
            }
            vehicle.setStatus(1);
            vehicle.setOwnerId(userId);
            vehicle.setCreatedOn(new Date());
            vehicle.setLastUpdatedOn(new Date());

            Vehicle v = repository.save(vehicle);
            v.setCreatedBy(userId);
            v.setLastUpdatedBy(userId);
            repository.save(v);
            return v;
        } catch (Exception e) {
            System.out.println(e.getMessage()); //TODO show this message
        }
        return null;
    }

    private static boolean checkIfVehicleExists(String vin, VehicleRepository repo) {
        List<Vehicle> vehicles = repo.findVehicleByVin(vin);
        if (vehicles.isEmpty()) {
            return false;
        }
        return true;
    }

    public static Rent populateRentInfo(Integer userId, Vehicle vehicle, String startDate, String endDate) throws ParseException {
        Rent rentInfo = new Rent();
        rentInfo.setOwnerId(vehicle.getOwnerId());
        rentInfo.setVehicle(vehicle);
        rentInfo.setRenterId(userId);
        rentInfo.setStartDate(CommonMethods.formatDate(startDate));
        rentInfo.setEndDate(CommonMethods.formatDate(endDate));
        Double totalPrice = CommonMethods.calculateTotalCost(vehicle.getPrice(), startDate, endDate);
        rentInfo.setPrice(totalPrice);
        //rentInfo.setDeposit();
        rentInfo.setFine(null);
        rentInfo.setStatus(RentStatus.ACTIVE.getId());
        rentInfo.setCreatedBy(userId);
        rentInfo.setCreatedOn(new Date());
        rentInfo.setLastUpdatedOn(new Date());
        rentInfo.setLastUpdatedBy(userId);
        return rentInfo;
    }

    public static List<Vehicle> fetchVehicles(VehicleRepository vehicleRepository, String keyword,
                                              String startDate, String endDate) {
        List<Vehicle> cars;
        if (keyword.isEmpty()) {
            cars = vehicleRepository.findAll();
        } else {
            String key = "%"+keyword+"%";
            cars = vehicleRepository.findVehiclesByNativeQuery(key, startDate, endDate);
        }
        return cars;
    }

    public static List<Vehicle> fetchVehiclesByType(VehicleRepository vehicleRepository, String city,
                                              String startDate, String endDate, String type) {
        List<Vehicle> cars;
        if (city.isEmpty()) {
            cars = vehicleRepository.findAll();
        } else {
            String key = "%"+city+"%";
            cars = vehicleRepository.findVehiclesByNativeQueryAndType(key, startDate, endDate, type);
        }
        return cars;
    }

    public static List<Vehicle> fetchVehiclesByBuild(VehicleRepository vehicleRepository, String city,
                                              String startDate, String endDate, String build) {
        List<Vehicle> cars;
        if (city.isEmpty()) {
            cars = vehicleRepository.findAll();
        } else {
            String key = "%"+city+"%";
            cars = vehicleRepository.findVehiclesByNativeQueryAndBuild(key, startDate, endDate, build);
        }
        return cars;
    }
}
