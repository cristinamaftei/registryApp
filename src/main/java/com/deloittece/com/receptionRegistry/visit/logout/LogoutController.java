package com.deloittece.com.receptionRegistry.visit.logout;

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
public class LogoutController {

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

    @RequestMapping(value = "/badgereturn", method = RequestMethod.GET)
    public String showVisitorDetails(@RequestParam("search") String icInfo, Model model) {

        Visitor visitorFound = visitorRepository.findByIdentityCardInfo(icInfo);
        Visit actualVisit = null;
        if (visitorFound != null) {
            List<Visit> visitsForAVisitor = visitorFound.getVisits();
            //case1: the visitor has only one visit
            if (visitsForAVisitor.size() == 1) {
                actualVisit = visitsForAVisitor.get(0);
                if (actualVisit.getDateOfExit() == null) {
                    model.addAttribute("visitor", visitorFound);
                    model.addAttribute("employee", actualVisit.getEmployee());
                    model.addAttribute("visit", actualVisit);
                    model.addAttribute("badgeTypes", BadgeType.values());
                } else {
                    return "cannotlogout";
                }

            } else {
                //case2: the visitor has more visits so the most recent one will be displayed
                for (int i = 0; i < visitsForAVisitor.size(); i++) {
                    for (int j = visitsForAVisitor.size() - 1; j >= 0; j--) {
                        if (visitsForAVisitor.get(i).getTimestamp().before(visitsForAVisitor.get(j).getTimestamp())) {
                            actualVisit = visitsForAVisitor.get(j);
                            if (actualVisit.getDateOfExit() == null) {

                                model.addAttribute("visitor", visitorFound);
                                model.addAttribute("employee", actualVisit.getEmployee());
                                model.addAttribute("visit", actualVisit);
                                model.addAttribute("badgeTypes", BadgeType.values());
                            } else {
                                return "cannotlogout";
                            }
                        }

                    }
                }
            }
        } else {
            return "cannotlogout";
        }

        return "badgereturn";

    }

    //when the visitor is logging out the date of exit will be set
    //if the visitor doesn't logout the date of exit is set automatically after one week and the flag exitWasSetAuto will be true
    @RequestMapping(value = "/savevisitorlogout", method = RequestMethod.POST)
    public void saveVisitForLogout(Visitor visitor, Visit visit, HttpServletResponse response) throws IOException {
        if (visit.getDateOfExit() == null) {
            visit.setDateOfExit(new Date());
            visitRepository.save(visit);
        }

        response.sendRedirect("/index");
    }
}