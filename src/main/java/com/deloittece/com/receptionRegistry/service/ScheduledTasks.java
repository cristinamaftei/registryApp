package com.deloittece.com.receptionRegistry.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.deloittece.com.receptionRegistry.database.VisitRepository;
import com.deloittece.com.receptionRegistry.database.VisitorRepository;

@Component
public class ScheduledTasks {
	@Autowired
	private VisitRepository visitRepository;

	// runs every Monday at 7
	//Seconds Minutes Hours  Day of month   Month   Day of week
	//0 7 * * 1 ?    */5 * * * * ?   "* 0 7 * * 1"
	//runs every Monday at 7am
	@Scheduled(cron = "* 0 7 * * 1")
	public void updateLogoutInformation() {
		System.out.println("****************In task ***************");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -8);

		java.sql.Date oneWeek = new java.sql.Date(cal.getTimeInMillis());

		visitRepository.updateExitDate(oneWeek);
	}
}