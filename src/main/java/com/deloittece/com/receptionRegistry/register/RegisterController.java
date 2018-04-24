package com.deloittece.com.receptionRegistry.register;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;

import org.jxls.template.SimpleExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deloittece.com.receptionRegistry.database.BadgeType;
import com.deloittece.com.receptionRegistry.database.Employee;
import com.deloittece.com.receptionRegistry.database.EmployeeRepository;
import com.deloittece.com.receptionRegistry.database.Visit;
import com.deloittece.com.receptionRegistry.database.VisitRepository;
import com.deloittece.com.receptionRegistry.database.Visitor;
import com.deloittece.com.receptionRegistry.database.VisitorRepository;

@Controller
public class RegisterController {
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

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String home() {
		return "index";
	}
	//see all the reports
	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	public String reports() {
		return "reports";
	}

	@RequestMapping(value = "/visitors", method = RequestMethod.GET)
	public String visitorsList(Model model) {
		model.addAttribute("visitors", visitorRepository.findAll());
		return "visitors";
	}

	@RequestMapping(value = "/visits", method = RequestMethod.GET)
	public String visitsList(Model model) {
		model.addAttribute("visitors", visitorRepository.findAll());
		model.addAttribute("employees", employeeRepository.findAll());
		model.addAttribute("visits" , visitRepository.findAll());
		return "visits";
	}
	//export visits list to excel
	@RequestMapping(value = "/exportVisits", method = RequestMethod.GET)
	public void export(HttpServletResponse response) {
		List<Visit> visits = (List<Visit>) visitRepository.findAll();
		List<String> headers = Arrays.asList("Arrival date", "Arrival time", "Visitor name", "Deloitte employee visited", "Badge no", "Badge type", "Date of badge receive", "Date of exit", "Did the visitor logout");
		try {
			response.addHeader("Content-disposition", "attachment; filename=Visits.xls");
			response.setContentType("application/vnd.ms-excel");
			new SimpleExporter().gridExport(headers, visits, "arrivalDate, arrivalTime, visitor.fullName, employee.empFullName, badgeNumber, badgeType, dateOfBadgeReceive, dateOfExit, exitWasSetAuto ", response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//exports visitors list to excel
	@RequestMapping(value = "/exportVisitors", method = RequestMethod.GET)
	public void exportVisitors(HttpServletResponse response) {
		List<Visitor> visitors = (List<Visitor>) visitorRepository.findAll();
		List<String> headers = Arrays.asList("Name", "Identity card info", "E-mail");
		try {
			response.addHeader("Content-disposition", "attachment; filename=Visitors.xls");
			response.setContentType("application/vnd.ms-excel");
			new SimpleExporter().gridExport(headers, visitors, "fullName, identityCardInfo, email", response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public String employeesList(Model model) {
		model.addAttribute("employees", employeeRepository.findAll());
		return "employees";
	}

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
			if (!visit.getBadgeNumber().equals(null)) {
				visit.setDateOfBadgeReceive(new Date());
			}
			
			visitorRepository.save(visitor);
			visit.setVisitor(visitor);
			visit.setEmployee(emp);
			visitRepository.save(visit);
			response.sendRedirect("/index");
			return "index";
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String searchPage() {

		return "search";
	}

	@RequestMapping(value = "/search2", method = RequestMethod.GET)
	public String search2Page() {

		return "search2";
	}

	@RequestMapping(value = "/badgereturn", method = RequestMethod.GET)
	public String showVisitorDetails(@RequestParam("search") String icInfo, Model model) {

		Visitor visitorFound = visitorRepository.findByIdentityCardInfo(icInfo);
		Visit actualVisit = null;
		if (visitorFound != null) {
			List<Visit> visitsForAVisitor = visitorFound.getVisits();

			if (visitsForAVisitor.size() == 1) {
				actualVisit = visitsForAVisitor.get(0);
				if(actualVisit.getDateOfExit() == null) {
				model.addAttribute("visitor", visitorFound);
				model.addAttribute("employee", actualVisit.getEmployee());
				model.addAttribute("visit", actualVisit);
				model.addAttribute("badgeTypes", BadgeType.values());
				} else {
					return "cannotlogout";
				}
				
			} else {
				for (int i = 0; i < visitsForAVisitor.size(); i++) {
					for (int j = visitsForAVisitor.size() - 1; j >= 0; j--) {
						if (visitsForAVisitor.get(i).getTimestamp().before(visitsForAVisitor.get(j).getTimestamp())) {
							actualVisit = visitsForAVisitor.get(j);
							if(actualVisit.getDateOfExit() == null) {

							model.addAttribute("visitor", visitorFound);
							model.addAttribute("employee", actualVisit.getEmployee());
							model.addAttribute("visit", actualVisit);
							model.addAttribute("badgeTypes", BadgeType.values());
							}else {
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

	@RequestMapping(value = "/savevisitorlogout", method = RequestMethod.POST)
	public void saveVisitForLogout(Visitor visitor, Visit visit, HttpServletResponse response) throws IOException {
		if (visit.getDateOfExit() == null) {
			visit.setDateOfExit(new Date());
			visitRepository.save(visit);
		}

		response.sendRedirect("/index");
	}

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
			// Mark visit details.
			visit.setTimestamp(new Timestamp(System.currentTimeMillis()));
			visit.setArrivalDate(new Date());
			visit.setArrivalTime(Calendar.getInstance().getTime());
			if (visit.getBadgeNumber() != null) {
				visit.setDateOfBadgeReceive(new Date());
			}
			visit.setVisitor(visitor);
			visit.setEmployee(emp);
			visitRepository.save(visit);
			response.sendRedirect("/index");
			return "index";
		}
	}
	//used for employee search
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
	public @ResponseBody List<Employee> getEmployees(@RequestParam String empFullName) {
		return simulateSearchResult(empFullName);

	}
//	
//	@AssertTrue( message = "You have to choose a badge type")
//	  private boolean isBadgeTypeRequired(Visit visit) {
//	    return visit.getBadgeNumber() != null || visit.getBadgeType() != null;
//	  }

}