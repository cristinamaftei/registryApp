package com.deloittece.com.receptionRegistry.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.deloittece.com.receptionRegistry.database.VisitRepository;

@Component
public class ScheduledTasks {
	@Autowired
	private VisitRepository visitRepository;

	// updates the date of exit automatically after one week, for visitors that don't logout
	// runs every Monday at 7am
	// Seconds Minutes Hours Day of month Month Day of week
	@Scheduled(cron = "* 0 7 * * 1")
	public void updateLogoutInformation() {
		System.out.println("****************In task ***************");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -8);

		java.sql.Date oneWeek = new java.sql.Date(cal.getTimeInMillis());

		visitRepository.updateExitDate(oneWeek);
	}
}