package com.example.controller;

import com.example.model.entity.User;
import com.example.model.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    // Trang đăng ký
    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    // Xử lý đăng ký
    @PostMapping("/register")
    public String handlerRegister(@ModelAttribute("user") User user) {
        if (userService.register(user)) {
            return "redirect:/login"; // Chuyển đến trang đăng nhập nếu đăng ký thành công
        }
        return "register"; // Quay lại trang đăng ký nếu thất bại
    }

    // Trang đăng nhập
    @GetMapping("/login")
    public String login(Model model,
                        @CookieValue(required = false, name = "userCookieEmail") String userCookieEmail,
                        @CookieValue(required = false, name = "userPassword") String userPassword) {
        User user = new User();
        user.setEmail(userCookieEmail); // Thiết lập email từ cookie nếu có
        user.setPassword(userPassword); // Thiết lập password từ cookie nếu có
        model.addAttribute("user", user);
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String handlerLogin(@ModelAttribute("user") User user,
                               Model model,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestParam(name = "check", required = false) boolean check) {
        User userLogin = userService.login(user);
        if (userLogin != null) {
            // Lưu thông tin user vào session
            HttpSession session = request.getSession();
            session.setAttribute("userLogin", userLogin);
            if (check) {
                // Tạo cookie nếu checkbox "ghi nhớ đăng nhập" được chọn
                Cookie cookieEmail = new Cookie("userCookieEmail", user.getEmail());
                cookieEmail.setMaxAge(24 * 60 * 60); // Cookie tồn tại trong 1 ngày
                Cookie cookiePassword = new Cookie("userPassword", user.getPassword());
                cookiePassword.setMaxAge(24 * 60 * 60);
                response.addCookie(cookieEmail);
                response.addCookie(cookiePassword);
            } else {
                // Xóa cookie nếu không chọn "ghi nhớ đăng nhập"
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("userCookieEmail") || cookie.getName().equals("userPassword")) {
                            cookie.setMaxAge(0); // Đặt thời gian tồn tại của cookie là 0 để xóa
                            response.addCookie(cookie);
                        }
                    }
                }
            }
            // Chuyển hướng về trang chủ sau khi đăng nhập thành công
            return "redirect:/";
        }
        // Nếu thông tin đăng nhập sai, quay lại trang login và báo lỗi
        model.addAttribute("err", "Sai thông tin tài khoản");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("userLogin");
        return "redirect:/";
    }

}
