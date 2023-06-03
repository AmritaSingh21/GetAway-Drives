package com.example.getawaydrives.service;

import com.example.getawaydrives.entities.Status;
import com.example.getawaydrives.entities.User;
import com.example.getawaydrives.entities.UserType;
import com.example.getawaydrives.repositories.UserRepository;
import com.example.getawaydrives.utility.CommonMethods;
import com.example.getawaydrives.utility.PasswordAuthentication;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class UserServiceImpl {

    public static User registerUser(User user, UserRepository repository) throws Exception {
        if (checkIfEmailExists(user.getEmail(), repository)) {
            throw new Exception("Email already exists!");
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            throw new Exception("Password Mismatch!");
        }
        user.setStatus(1);
        user.setUserType(1);
        user.setCreatedOn(new Date());
        user.setLastUpdatedOn(new Date());
        user.setPassword(convertPassword(user.getPassword()));
        if (user.getDlExpiryDateString() != null) {
            user.setDlExpiryDate(CommonMethods.formatDate(user.getDlExpiryDateString()));
        }

        User u = repository.save(user);
        u.setCreatedBy(u.getId());
        u.setLastUpdatedBy(u.getId());
        repository.save(u);
        return u;
    }

    private static String convertPassword(String pw) {
        PasswordAuthentication authentication = new PasswordAuthentication();
        return authentication.hash(pw);
    }

    public static boolean checkIfEmailExists(String email, UserRepository repo) {
        List<User> users = repo.findUserByEmail(email);
        if (users.isEmpty()) {
            return false;
        }
        return true;
    }

    public static User loginUser(User loginCred, UserRepository repository) throws Exception {
        List<User> users = repository.findUserByEmail(loginCred.getEmail());
        PasswordAuthentication auth = new PasswordAuthentication();
        if (users.isEmpty()) {
            throw new Exception("Invalid Credentials!");
        }
        User user = users.get(0);
        if (!auth.authenticate(loginCred.getPassword(), user.getPassword())) {
            throw new Exception("Incorrect Password!");
        }
        return user;
    }

    public static User editUser(User user, UserRepository repository) {
        try {
            List<User> users = repository.findUserByEmail(user.getEmail());
            if (users.size() > 1) {
                throw new Exception("Email already exists!");
            }
            user.setLastUpdatedOn(new Date());
            user.setLastUpdatedBy(user.getId());
            repository.save(user);
            return user;
        } catch (Exception e) {
            System.out.println(e.getMessage()); //TODO show this message
        }
        return null;
    }

    public static User mapUserRequest(User user, User userRequest) throws ParseException {
        userRequest.setId(user.getId());
        if(userRequest.getEmail() == null){
            userRequest.setEmail(user.getEmail());
        }
        if(userRequest.getPassword() == null){
            userRequest.setPassword(user.getPassword());
        }
        if(userRequest.getFirstName() == null){
            userRequest.setFirstName(user.getFirstName());
        }
        if(userRequest.getLastName() == null){
            userRequest.setLastName(user.getLastName());
        }
        if(userRequest.getAddress() == null){
            userRequest.setAddress(user.getAddress());
        }
        if(userRequest.getPhoneNumber() == null){
            userRequest.setPhoneNumber(user.getPhoneNumber());
        }
        userRequest.setDlNumber(user.getDlNumber());
        userRequest.setDlExpiryDate(user.getDlExpiryDate());
        userRequest.setDlIssuingCountry(user.getDlIssuingCountry());
        userRequest.setUserType(UserType.CUSTOMER.getId());
        userRequest.setStatus(Status.ACTIVE.getId());
        userRequest.setCreatedOn(user.getCreatedOn());
        userRequest.setCreatedBy(user.getCreatedBy());
        return userRequest;
    }
}
