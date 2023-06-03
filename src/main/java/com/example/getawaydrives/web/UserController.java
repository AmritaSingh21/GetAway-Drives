package com.example.getawaydrives.web;

import com.example.getawaydrives.entities.DocumentType;
import com.example.getawaydrives.entities.User;
import com.example.getawaydrives.repositories.DocumentRepository;
import com.example.getawaydrives.repositories.UserRepository;
import com.example.getawaydrives.service.UserServiceImpl;
import com.example.getawaydrives.utility.CommonMethods;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@SessionAttributes({"userId", "firstName"})
public class UserController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private DocumentRepository docRepo;

    @PostMapping("/login")
    public String login(Model model, User loginCred, BindingResult
            bindingResult, ModelMap mm, HttpSession session) {
        String err = "";
        if (bindingResult.hasErrors()) {
            return "loginForm";
        }
        try {
            User user = UserServiceImpl.loginUser(loginCred, repository);
            //ssession.setAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("firstName", user.getFirstName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("err", e.getMessage());
            err = e.getMessage();
        }
        if (err.isEmpty()) {
            return "LoggedIn";
        } else
            return "loginForm";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session, SessionStatus status) {
        status.setComplete();
        session.invalidate();
        return "redirect:index";
    }


    @PostMapping(path = "/register")
    public String register(Model model, @Valid User user, BindingResult
            bindingResult, ModelMap mm, HttpSession session) {
        String err = "";
        if (bindingResult.hasErrors()) {
            System.out.println("error: " + bindingResult.getFieldError());
            if (bindingResult.hasFieldErrors()) {
                err = bindingResult.getFieldError().getDefaultMessage();
                model.addAttribute("error", err);
            }
            return "Register";
        }
        try {
            CommonMethods.checkPasswordPattern(user.getPassword());
            User u = UserServiceImpl.registerUser(user, repository);
            if (u != null && user.getFile() != null) {
                CommonMethods.createDocument(docRepo, user.getFile(), u.getId(), null,
                        DocumentType.DL.getId());
            }
        } catch (Exception e) {
            err = e.getMessage();
            System.out.println(err);
            model.addAttribute("error", err);
        }
        if (err.isEmpty()) {
            return "redirect:index";
        } else
            return "Register";

    }

    @GetMapping(path = "/Register")
    public String regPage(Model model) {
        return "Register";
    }

    @GetMapping(path = "/userProfile")
    public String userProf(Model model, HttpSession session) {
        Object s = session.getAttribute("userId");
        int id = Integer.parseInt(s.toString());
        if (id != 0) {
            List<User> user = repository.findUserById(id);
            model.addAttribute("user", user);
        }
        return "userProfile";

    }

    @GetMapping(path = "/editProfile")
    public String editProf(Model model, HttpSession session) {
        Object s = session.getAttribute("userId");
        int id = Integer.parseInt(s.toString());
        List<User> user = repository.findUserById(id);
        if (!user.isEmpty()) {
            model.addAttribute("user", user.get(0));
        }
        return "editProfile";
    }

    @GetMapping(path = "/loginPage")
    public String loginPage(Model model) {
        return "loginForm";
    }

    @GetMapping(path = "/loggedIn")
    public String loggedIn(Model model) {
        return "LoggedIn";
    }

    @PostMapping("/editUser")
    public String editUser(Model model,User userRequest, BindingResult
            bindingResult, ModelMap mm, HttpSession session) {
        try {
            Integer id = (Integer) session.getAttribute("userId");
            List<User> users = repository.findUserById(id);
            User user = users.get(0);
            userRequest = UserServiceImpl.mapUserRequest(user, userRequest);
            UserServiceImpl.editUser(userRequest, repository);
            if (userRequest.getFile() != null) {
                CommonMethods.createDocument(docRepo, userRequest.getFile(), id, null,
                        DocumentType.DL.getId());
            }
            List<User> uList = new ArrayList<>();
            uList.add(userRequest);
            model.addAttribute("user", uList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "userProfile";
    }

    @GetMapping(path = "/home")
    public String vehicles(Model model, HttpSession session) {
        Object att = session.getAttribute("userId");
        if (att == null) {
            return "redirect:index";
        } else
            return "LoggedIn";
    }

    @InitBinder     /* Converts empty strings into null when a form is submitted */
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
