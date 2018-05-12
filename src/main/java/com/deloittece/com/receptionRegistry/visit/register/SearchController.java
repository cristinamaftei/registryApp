package com.deloittece.com.receptionRegistry.visit.register;

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

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String goToRegisterPage(Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("visitor", new Visitor());
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("badgeTypes", BadgeType.values());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveVisitor(@ModelAttribute @Valid Visitor visitor, BindingResult bindingRes,
                              @ModelAttribute @Valid Visit visit, BindingResult bindingResult, @ModelAttribute Employee emp,
                              HttpServletResponse response, Model model) throws IOException {

        List<Visitor> visitors = (List<Visitor>) visitorRepository.findAll();
        List<String> icInfosList = visitors.stream().map(Visitor::getIdentityCardInfo).collect(Collectors.toList());
        if (bindingResult.hasErrors() || bindingRes.hasErrors()) {
            model.addAttribute("employee", employeeRepository.findAll());
            model.addAttribute("badgeTypes", BadgeType.values());
            return "register";
        } else {
            for (int i = 0; i < icInfosList.size(); i++) {
                if (icInfosList.get(i).equals(visitor.getIdentityCardInfo())) {
                    return "erroricinfo";
                }
            }
            visit.setTimestamp(new Timestamp(System.currentTimeMillis()));
            visit.setArrivalDate(new Date());
            visit.setArrivalTime(Calendar.getInstance().getTime());

            if (visit.getBadgeNumber() != null) {
                visit.setDateOfBadgeReceive(new Date());
            }

            visitorRepository.save(visitor);
            visit.setVisitor(visitor);
            visit.setEmployee(emp);
            visitRepository.save(visit);
            response.sendRedirect("/confirm");
            return "confirm";
        }
    }


    //this search is used when the visitor returns to Deloitte
    @RequestMapping(value = "/othervisit", method = RequestMethod.GET)
    public String showVisitorDetailsOtherVisit(@RequestParam("search2") String icInfo, Model model) {

        Visitor visitorFound = visitorRepository.findByIdentityCardInfo(icInfo);
        if (visitorFound != null) {
            model.addAttribute("visit", new Visit());
            model.addAttribute("visitor", visitorFound);
            model.addAttribute("employees", employeeRepository.findAll());
            model.addAttribute("badgeTypes", BadgeType.values());
        } else {
            return "cannotlogout";
        }
        return "othervisit";
    }

    @RequestMapping(value = "/saveothervisit", method = RequestMethod.POST)
    public String saveOtherVisit(@ModelAttribute @Valid Visitor visitor, BindingResult bindingRes,
                                 @ModelAttribute @Valid Visit visit, BindingResult bindingResult, @ModelAttribute Employee emp,
                                 HttpServletResponse response, Model model) throws IOException {

        if (bindingResult.hasErrors() || bindingRes.hasErrors()) {
            model.addAttribute("employees", employeeRepository.findAll());
            model.addAttribute("badgeTypes", BadgeType.values());
            return "othervisit";
        } else {
            visit.setTimestamp(new Timestamp(System.currentTimeMillis()));
            visit.setArrivalDate(new Date());
            visit.setArrivalTime(Calendar.getInstance().getTime());
            if (visit.getBadgeNumber() != null) {
                visit.setDateOfBadgeReceive(new Date());
            }
            visit.setVisitor(visitor);
            visit.setEmployee(emp);
            visitRepository.save(visit);
            response.sendRedirect("/confirm");
            return "confirm";
        }
    }
}