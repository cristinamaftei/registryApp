package com.deloittece.com.receptionRegistry.reports;

import com.deloittece.com.receptionRegistry.database.Visit;
import com.deloittece.com.receptionRegistry.database.VisitRepository;
import com.deloittece.com.receptionRegistry.database.Visitor;
import com.deloittece.com.receptionRegistry.database.VisitorRepository;
import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    // see all the reports
    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public String reports() {
        return "reports";
    }

    // export visits list to excel so custom reports can be made
    // for more info: http://blog.exxeta.com/java/2016/08/03/simple-excel-export-spring-based-web-application/
    @RequestMapping(value = "/exportVisits", method = RequestMethod.GET)
    public void export(HttpServletResponse response) {
        List<Visit> visits = (List<Visit>) visitRepository.findAll();
        List<String> headers = Arrays.asList("Arrival date", "Arrival time", "Visitor name",
                "Deloitte employee visited", "Badge no", "Badge type", "Date of badge receive", "Date of exit",
                "Did the visitor logout");
        try {
            response.addHeader("Content-disposition", "attachment; filename=Visits.xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(headers, visits,
                    "arrivalDate, arrivalTime, visitor.fullName, employee.empFullName, badgeNumber, badgeType, dateOfBadgeReceive, dateOfExit, exitWasSetAuto ",
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // exports visitors list to excel so custom reports can be made
    @RequestMapping(value = "/exportVisitors", method = RequestMethod.GET)
    public void exportVisitors(HttpServletResponse response) {
        List<Visitor> visitors = (List<Visitor>) visitorRepository.findAll();
        List<String> headers = Arrays.asList("Name", "Identity card info", "E-mail");
        try {
            response.addHeader("Content-disposition", "attachment; filename=Visitors.xls");
            response.setContentType("application/vnd.ms-excel");
            new SimpleExporter().gridExport(headers, visitors, "fullName, identityCardInfo, email",
                    response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}