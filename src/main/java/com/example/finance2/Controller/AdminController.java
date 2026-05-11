package com.example.finance2.Controller;
import com.example.finance2.Model.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/login")
    public String adminLogin(@RequestBody LoginRequest loginData){

        String username = loginData.getUsername();
        String password = loginData.getPassword();

        if ("admin".equals(username) && "1234".equals(password)){
            return "Inloggning lyckades! Välkommen admin";
        } else {
            return "Fel användarnamn eller lösenord";
        }
    }
}
