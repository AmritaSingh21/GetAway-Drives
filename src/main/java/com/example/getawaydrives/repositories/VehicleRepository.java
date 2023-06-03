package com.example.getawaydrives.repositories;

import com.example.getawaydrives.entities.Vehicle;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Qualifier("vehicle")
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    List<Vehicle> findByModelContaining(String model);

    List<Vehicle> findVehicleByVin(String vin);
    //List<Vehicle> findByNameContaining (String kw);
    List<Vehicle> findByBuildAndCity(String build,String city);
    List<Vehicle> findByType(String type);
    List<Vehicle> findByTypeAndCity(String type,String city);

    List<Vehicle> findVehicleById(Integer id);

    List<Vehicle> findByCityContaining(String city);

    @Query(value = "select v.*, r.id as rentId from vehicle v LEFT outer join rent r " +
            "on v.id=r.vehicle_id and r.status=1 where v.city like :city and (r.id is null " +
            "or (r.start_date>:end or r.end_date<:start))", nativeQuery = true)
    List<Vehicle> findVehiclesByNativeQuery(@Param("city") String city, @Param("start") String startDate,
                                            @Param("end") String endDate);

    @Query(value = "select v.*, r.id as rentId from vehicle v LEFT outer join rent r " +
            "on v.id=r.vehicle_id and r.status=1 where v.city like :city and v.type=:type and (r.id is null " +
            "or (r.start_date>:end or r.end_date<:start))", nativeQuery = true)
    List<Vehicle> findVehiclesByNativeQueryAndType(@Param("city") String city, @Param("start") String startDate,
                                            @Param("end") String endDate, @Param("type") String type);

    @Query(value = "select v.*, r.id as rentId from vehicle v LEFT outer join rent r " +
            "on v.id=r.vehicle_id and r.status=1 where v.city like :city and v.build=:build and (r.id is null " +
            "or (r.start_date>:end or r.end_date<:start))", nativeQuery = true)
    List<Vehicle> findVehiclesByNativeQueryAndBuild(@Param("city") String city, @Param("start") String startDate,
                                            @Param("end") String endDate, @Param("build") String build);
}
