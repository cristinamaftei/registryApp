package com.deloittece.com.receptionRegistry.register;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
	
	@RequestMapping(value ="/index", method = RequestMethod.GET)
	public String home() {
		return "index";
	}
	
	@RequestMapping(value ="/visitors", method = RequestMethod.GET)
	public String visitorsList(Model model) {
		model.addAttribute("visitors", visitorRepository.findAll());
		return "visitors";
	}
	
	@RequestMapping(value ="/visits", method = RequestMethod.GET)
	public String visitsList(Model model) {
		model.addAttribute("visitors", visitorRepository.findAll());
		model.addAttribute("employee", employeeRepository.findAll());
		return "visits";
	}
	
	@RequestMapping(value ="/employees", method = RequestMethod.GET)
	public String employeesList(Model model) {
		model.addAttribute("employees", employeeRepository.findAll());
		return "employees";
	}	
	
	@RequestMapping(value ="/register", method = RequestMethod.GET)
	public String goToRegisterPage(Model model) {
		model.addAttribute("visit", new Visit());
		model.addAttribute("visitor", new Visitor());
		model.addAttribute("employees", employeeRepository.findAll());
		model.addAttribute("fileName", new Date());
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveVisitor( @ModelAttribute @Valid Visitor visitor, BindingResult bindingRes, @ModelAttribute @Valid Visit visit, BindingResult bindingResult, @ModelAttribute Employee emp,
			HttpServletResponse response, Model model) throws IOException {
				
		List<Visitor> visitors = (List<Visitor>) visitorRepository.findAll();
		List<String> icInfosList = visitors.stream().map(Visitor::getIdentityCardInfo).collect(Collectors.toList());
		
		if (bindingResult.hasErrors() || bindingRes.hasErrors()) {
			model.addAttribute("employees", employeeRepository.findAll());
			return "register";
		} else {
			// Mark visit details.
			
			for (int i = 0; i < icInfosList.size(); i++) {
				if(icInfosList.get(i).equals(visitor.getIdentityCardInfo())) {
					return "erroricinfo";
				}
			}
			visit.setArrivalDate(new Date());
			visit.setArrivalTime(Calendar.getInstance().getTime());
			if (visit.getBadgeNumber() != null) {
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

//	public boolean isIcInfoUnique(String icInfoNew, Visitor visitor) {
//		
//		List<Visitor> visitors = (List<Visitor>) visitorRepository.findAll();
//		List<String> icInfosList = visitors.stream().map(Visitor::getIdentityCardInfo).collect(Collectors.toList());
//
//		for (int i = 0; i < icInfosList.size(); i++) {
//			if(icInfosList.get(i).equals(visitor.getIdentityCardInfo())) {
//				return false;
//			}else
//				return true;
//		}
//		return true;
//		
//	}
//		
//		for (int i = 0; i < icInfosList.size(); i++) {
//			for (int j= icInfosList.size() -1; j >= 0 ; j--) {
//				if (icInfosList.get(i).equals(icInfosList.get(j))) {
//					return false;
//				}else {
//					return true;
//				}
//			}
//		}
//		return true;	
	


	@RequestMapping(value ="/search", method = RequestMethod.GET)
	public String searchPage() {
		
		return "search";
	}
	
	@RequestMapping(value ="/search2", method = RequestMethod.GET)
	public String search2Page() {
		
		return "search2";
	}
	
	@RequestMapping(value = "/badgereturn", method = RequestMethod.GET)
	public String showVisitorDetails(@RequestParam("search") String icInfo, Model model) {
		
		Visitor visitorFound = visitorRepository.findByIdentityCardInfo(icInfo);
		Visit actualVisit = null;
		if (visitorFound != null) {
			List<Visit> visitsForAVisitor = visitorFound.getVisits();
			
			if(visitsForAVisitor.size() ==1) {
				actualVisit = visitsForAVisitor.get(0);
				model.addAttribute("visitor", visitorFound);
				model.addAttribute("employee", actualVisit.getEmployee());
				model.addAttribute("visit", actualVisit);	
			}else {
				for (int i = 0; i < visitsForAVisitor.size(); i++) {
					for (int j = visitsForAVisitor.size()-1; j >=0; j--) {
						if (visitsForAVisitor.get(i).getArrivalDate().before(visitsForAVisitor.get(j).getArrivalDate())) {
							actualVisit = visitsForAVisitor.get(j);

							//if (!visitorFound.isUserLogoutAuto()) {
								model.addAttribute("visitor", visitorFound);
								model.addAttribute("employee", actualVisit.getEmployee());
								model.addAttribute("visit", actualVisit);								
							//} else {
								//return "cannotlogout";
							}

						}
					}
				}
		} else {
			return "cannotlogout";
		}
		
		return "badgereturn";

		//dateOfExit != null -> check!!!!
	}

	@RequestMapping(value = "/savevisitorlogout", method = RequestMethod.POST)
	public void saveVisitorForLogout(Visitor visitor, Visit visit, HttpServletResponse response) throws IOException {
		if (visit.getDateOfExit() == null) {
			visit.setDateOfExit(new Date());
			//visitor.setUserLogoutAuto(true);
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
		} else {
			return "cannotlogout";
		}
		return "othervisit";

	}
	
	@RequestMapping(value ="/saveothervisit", method = RequestMethod.POST)
	public String saveOtherVisit(@ModelAttribute @Valid Visitor visitor, BindingResult bindingRes, @ModelAttribute @Valid Visit visit, BindingResult bindingResult, @ModelAttribute Employee emp,
			HttpServletResponse response, Model model) throws IOException {
		
		if (bindingResult.hasErrors() || bindingRes.hasErrors()) {
			model.addAttribute("employees", employeeRepository.findAll());
			return "register";
		} else {
			// Mark visit details.
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
		
		
		
		
		
		
		
		
		
//		//visit.setVisitor(visitor);
//		if(visit.getDateOfExit() == null) {
//		visit.setDateOfExit(new Date());
//		
//		//visitor.setUserLogoutAuto(true);		
//		//visitorRepository.save(visitor);
//		//visitorRepository.save(visitor);
//		visitRepository.save(visit);
//		}
		
		//response.sendRedirect("/index");
	}

	
//	@RequestMapping(value = "/badgereturn", method = RequestMethod.GET)
//	public String showVisitorDetails(@RequestParam("search") String icInfo, Model model) {
//		Visitor visitorFound = null;
//		List<Visitor> visitors = (List<Visitor>) visitorRepository.findAll();
//		ListIterator<Visitor> iter = visitors.listIterator();
//		while (iter.hasNext()) {
//			if (iter.next().getIsUserLogoutAuto()) {
//				iter.remove();
//			}
//		}
//		for (Visitor visitor : visitors) {
//			if (visitor.getIdentityCardInfo().equals(icInfo)) {
//				visitorFound = visitor;
//				model.addAttribute("visitor", visitorFound);
//				model.addAttribute("employee", visitorFound.getEmployee());
//			}else {
//				return "cannotlogout";
//			}
//		}
//		
//		return "badgereturn";
	
	
//	}
	
//	@RequestMapping(value = "/signature", method = RequestMethod.GET)
//	public String signaturePage(Model model) {
//		model.addAttribute("fileName", new Date());
//		return "signature";
//		
//	}
//	@RequestMapping(value = "/signature", method = RequestMethod.POST)
//	public void saveSignature(Visitor visitor, HttpServletResponse response) throws IOException{
//		//visitorRepository.save(visitor);
//		//visitor.getId();
//		response.sendRedirect("/index");
//		
//	}
	


}
