package com.deloittece.com.receptionRegistry.search;

import com.deloittece.com.receptionRegistry.database.*;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    //used for validation
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @RequestMapping(value = "/visitors", method = RequestMethod.GET)
    public String visitorsList(Model model) {
        model.addAttribute("visitors", visitorRepository.findAll());
        return "visitors";
    }

    @RequestMapping(value = "/visits", method = RequestMethod.GET)
    public String visitsList(Model model) {
        model.addAttribute("visitors", visitorRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("visits", visitRepository.findAll());
        return "visits";
    }


    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public String employeesList(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employees";
    }

    @RequestMapping(value = "/searchForBadgeReturn", method = RequestMethod.GET)
    public String searchForBadgeReturn() {

        return "searchForBadgeReturn";
    }

    @RequestMapping(value = "/searchRecurringVisitor", method = RequestMethod.GET)
    public String searchRecurringVisitor() {

        return "searchRecurringVisitor";
    }

    // used for employee search
    private List<Employee> simulateSearchResult(String empFullName) {
        List<Employee> result = new ArrayList<Employee>();
        List<Employee> employees = (List<Employee>) employeeRepository.findAll();
        // iterate the list and filter by name
        for (Employee emp : employees) {
            if ((emp.getEmpFullName()).toLowerCase().contains(empFullName.toLowerCase())) {
                result.add(emp);
            }
        }
        return result;
    }

    @RequestMapping(value = "/getEmployees", method = RequestMethod.GET)
    public @ResponseBody
    List<Employee> getEmployees(@RequestParam String empFullName) {
        return simulateSearchResult(empFullName);

    }
}