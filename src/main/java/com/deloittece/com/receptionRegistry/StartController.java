package com.deloittece.com.receptionRegistry;

import com.deloittece.com.receptionRegistry.database.EmployeeRepository;
import com.deloittece.com.receptionRegistry.database.VisitRepository;
import com.deloittece.com.receptionRegistry.database.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StartController {

    //used for validation
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/confirm", method = RequestMethod.GET)
    public String confirmInfo() {
        return "confirm";
    }

    @RequestMapping(value = "/termsAndCondition", method = RequestMethod.GET)
    public String termsAndCondition() {
        return "termsAndCondition";
    }

    @RequestMapping(value = "/restricted", method = RequestMethod.GET)
    public String restrictedPage() {
        return "restricted";
    }

}