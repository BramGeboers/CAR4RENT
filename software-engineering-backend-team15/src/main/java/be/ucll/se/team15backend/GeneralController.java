package be.ucll.se.team15backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;


@CrossOrigin(origins = "*")
@Controller
public class GeneralController {

    //IS GEEN REST CONTROLLER!!!!!

    @RequestMapping("/")
    public String index() {
        return "redirect:/swagger-ui.html";
    }

}
