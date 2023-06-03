package com.example.getawaydrives.utility;

import com.example.getawaydrives.entities.Document;
import com.example.getawaydrives.repositories.DocumentRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class CommonMethods {

    public static void createDocument(DocumentRepository repo, MultipartFile[] file, Integer userId, Integer vehicleId,
                                      int docType) {
        try {
            for (MultipartFile f : file) {
                Document document = new Document();
                if (userId != null) {
                    document.setUserId(userId);
                }
                if (vehicleId != null) {
                    document.setVehicleId(vehicleId);
                }
                document.setName(f.getOriginalFilename());
                document.setContent(f.getBytes());
                document.setDocumentType(docType);
                document.setCreatedBy(userId);
                document.setCreatedOn(new Date());
                document.setLastUpdatedBy(userId);
                document.setLastUpdatedOn(new Date());
                repo.save(document);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage()); //TODO show this message
        }
    }

    public static Date formatDate(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateStr);
        return date;
    }

    public static String checkDateRange(String startDate, String endDate) throws Exception {
        Date start = formatDate(startDate);
        Date end = formatDate(endDate);
        if (start.compareTo(end) != -1) {
            return "Please select correct date range.";
        }
        return null;
    }

    public static Double calculateTotalCost(Double price, String startDate, String endDate) {
        Date date1 = null, date2 = null;
        long daysBetween = 0;
        try {
            date1 = formatDate(startDate);
            date2 = formatDate(endDate);
            long timeBetween = date2.getTime() - date1.getTime();
            daysBetween = timeBetween / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (price * daysBetween) + price;
    }

    public static void checkPasswordPattern(String password) throws Exception {
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()])(?=\\S+$).{8,20}$";
        boolean matches = Pattern.matches(pattern, password);
        if (!matches) {
            throw new Exception("Password must be 8-20 characters, should contain atleast 1 lower case, " +
                    "1 upper case, 1 digit and 1 special character (!@#$%^&*)");
        }
    }
}
