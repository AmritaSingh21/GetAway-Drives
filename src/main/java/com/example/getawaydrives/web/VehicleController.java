package com.example.getawaydrives.web;

import com.example.getawaydrives.entities.Document;
import com.example.getawaydrives.entities.DocumentType;
import com.example.getawaydrives.entities.Rent;
import com.example.getawaydrives.entities.Vehicle;
import com.example.getawaydrives.repositories.DocumentRepository;
import com.example.getawaydrives.repositories.RentRepository;
import com.example.getawaydrives.repositories.VehicleRepository;
import com.example.getawaydrives.service.VehicleServiceImpl;
import com.example.getawaydrives.utility.CommonMethods;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.RouteMatcher;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class VehicleController {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DocumentRepository docRepo;
    @Autowired
    private RentRepository rentRepo;

    @GetMapping(path = "/index")
    public String vehicles(Model model) {
        return "index";
    }

    @GetMapping(path = "/")
    public String vehicles2(Model model) {
        return "index";
    }

    @GetMapping(path = "/byBuild")
    public String build(Model model) {
        return "byBuild";
    }

    @GetMapping(path = "/types")
    public String types(Model model) {
        return "types";
    }

    @GetMapping(path = "/search_types")
    public String search_types(Model model, @RequestParam(name = "type", defaultValue = "")
    String type, @RequestParam(name = "city", defaultValue = "") String city,
                               @RequestParam(name = "startDate", defaultValue = "") String startDate,
                               @RequestParam(name = "endDate", defaultValue = "") String endDate, HttpSession session) {
        try {
            String err = CommonMethods.checkDateRange(startDate, endDate);
            if (err != null) {
                model.addAttribute("err", err);
                return "types";
            }
            List<Vehicle> cars;
            city.replace("+", " ");
            if (type.isEmpty()) {
                cars = VehicleServiceImpl.fetchVehicles(vehicleRepository, city, startDate, endDate);
            } else {
                cars = VehicleServiceImpl.fetchVehiclesByType(vehicleRepository, city, startDate, endDate, type);
            }
            model.addAttribute("by_type", cars);
            model.addAttribute("searched", "yes");
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "types";
    }

    @GetMapping(path = "/search_builds")
    public String search_builds(Model model, @RequestParam(name = "build", defaultValue = "")
    String build, @RequestParam(name = "city", defaultValue = "") String city,
                                @RequestParam(name = "startDate", defaultValue = "") String startDate,
                                @RequestParam(name = "endDate", defaultValue = "") String endDate, HttpSession session) {
        try {
            String err = CommonMethods.checkDateRange(startDate, endDate);
            if (err != null) {
                model.addAttribute("err", err);
                return "byBuild";
            }
            List<Vehicle> cars;
            if (build.isEmpty()) {
                cars = VehicleServiceImpl.fetchVehicles(vehicleRepository, city, startDate, endDate);
            } else {
                cars = VehicleServiceImpl.fetchVehiclesByBuild(vehicleRepository, city, startDate, endDate, build);
            }
            model.addAttribute("by_build", cars);
            model.addAttribute("searched", "yes");
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "byBuild";
    }

    @GetMapping(path = "/details")
    public String details(Model model, @RequestParam(name = "id", defaultValue = "")
    Integer id, @RequestParam(name = "startDate", defaultValue = "") String startDate,
                          @RequestParam(name = "endDate", defaultValue = "") String endDate) {

        List<Vehicle> car = null;
        //cars = vehicleRepository.findVehicleByVin(vin);
        car = vehicleRepository.findVehicleById(id);
        if (car.isEmpty()) {
            return "types";
        }
        model.addAttribute("details", car);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "details";
    }

    @GetMapping(path = "/listCar")
    public String listCar(Model model) {

        Vehicle vehicle = new Vehicle();
        model.addAttribute("added", false);
        model.addAttribute("vehicle", vehicle);
        return "listCar";
    }

    @PostMapping(path = "/listCar")
    public String listCar(@Valid Vehicle vehicle, Model model, BindingResult result) {

        if (!result.hasErrors()) {
            model.addAttribute("added", true);
            vehicleRepository.save(vehicle);
        } else {
            model.addAttribute("added", false);
            model.addAttribute("vehicle", vehicle);
        }
        return "listCar";
    }

    @GetMapping("/deleteCar")
    public String deleteCar(Long id) {
        vehicleRepository.deleteById(id);

        return "redirect:/index";
    }

    @GetMapping(path = "/search")
    public String search(Model model, @RequestParam(name = "keyword", defaultValue = "")
    String keyword, @RequestParam(name = "startDate", defaultValue = "") String startDate,
                         @RequestParam(name = "endDate", defaultValue = "") String endDate,
                         HttpSession session) {
        try {
            String err = CommonMethods.checkDateRange(startDate, endDate);
            if (err != null) {
                model.addAttribute("err", err);
                if (session.getAttribute("userId") == null) {
                    return "index";
                } else {
                    return "LoggedIn";
                }
            }
            List<Vehicle> cars = VehicleServiceImpl.fetchVehicles(vehicleRepository, keyword, startDate, endDate);
            model.addAttribute("list_cars", cars);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "generalSearch";
    }

    @PostMapping(path = "/addVehicle")
    public String addVehicle(Model model, @Valid Vehicle vehicle, BindingResult
            bindingResult, ModelMap mm, HttpSession session) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            return "Register";
        } else {
            Integer id = (Integer) session.getAttribute("userId");
            System.out.println("my id: " + id);
            Vehicle v = VehicleServiceImpl.addVehicle(vehicle, vehicleRepository, id);
            if (v != null && vehicle.getPics() != null) {
                CommonMethods.createDocument(docRepo, vehicle.getPics(), id, v.getId(),
                        DocumentType.VEHICLE_PHOTOS.getId());
            }
            if (v != null && vehicle.getDocs() != null) {
                CommonMethods.createDocument(docRepo, vehicle.getDocs(), id, v.getId(),
                        DocumentType.INSURANCE.getId());
            }
            if (session.getAttribute("userId") == null) {
                return "index";
            } else {
                return "LoggedIn";
            }
        }
    }

    @GetMapping(path = "/getVehiclePhoto")
    public void getVehiclePhoto(HttpServletResponse response, @RequestParam("id") int id) {
        try {
            List<Document> photos = docRepo.getDocumentsByVehicleIdAndDocumentType(id, DocumentType.VEHICLE_PHOTOS.getId());
            if (photos != null && !photos.isEmpty()) {
                Document doc = photos.get(0);
                InputStream inputStream = new ByteArrayInputStream(doc.getContent());
                IOUtils.copy(inputStream, response.getOutputStream());
            }
        } catch (IOException e) {
            System.out.println("Exception occurred while fetching vehicle photo for id:" + id);
        }
    }

    @GetMapping(path = "/rent")
    public String rentVehicle(Model model, @RequestParam("id") Integer vehicleId,
                              @RequestParam(name = "startDate", defaultValue = "") String startDate,
                              @RequestParam(name = "endDate", defaultValue = "") String endDate,
                              ModelMap mm, HttpSession session) {
        if(session.getAttribute("userId") == null){
            return "loginForm";
        }
        List<Vehicle> vehicle = new ArrayList<>();
        String err;
        try {
            vehicle = vehicleRepository.findVehicleById(vehicleId);
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                model.addAttribute("err", "Please Sign In to continue.");
                model.addAttribute("details", vehicle);
                return "details";
            }
            err = CommonMethods.checkDateRange(startDate, endDate);
            Rent rentInfo = VehicleServiceImpl.populateRentInfo(userId, vehicle.get(0), startDate, endDate);
            rentRepo.save(rentInfo);
        } catch (Exception e) {
            System.out.println("Exception occurred while renting vehicle " + vehicleId);
            e.printStackTrace();
            model.addAttribute("err", "Error occurred.");
            model.addAttribute("details", vehicle);
            return "details";
        }
        if (err != null) {
            model.addAttribute("err", err);
            model.addAttribute("details", vehicle);
            return "details";
        }
        return "LoggedIn";

    }

}
