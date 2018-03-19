package com.deloittece.com.receptionRegistry.database;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "visits")
public class Visit implements Persistable<Long>{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VISIT_ID")
	private Long visitId;
	
	//@Valid
	@ManyToOne
	@JoinColumn(name="VISITOR_ID")
	private Visitor visitor;
	
	@NotNull(message = "Please select one Deloitte employee")
	@ManyToOne //(fetch=FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name="EMPLOYEE_ID")
	private Employee employee;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "ARRIVAL_DATE", updatable = false)
	private Date arrivalDate;
	
	@Temporal(TemporalType.TIME)
	@Column(name = "ARRIVAL_TIME", updatable = false)
	private Date arrivalTime;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_EXIT")
	private Date dateOfExit;
	
	@Column(name = "BADGE_NUMBER")
	@Pattern(regexp = "^([0-9]{5,17})-?([0-9]{1,2})?$", message ="Please enter the correct number of the badge")
	private String badgeNumber;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_BADGE_RECEIVE", updatable = false)
	@ColumnDefault("null")
	private Date dateOfBadgeReceive;

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Visitor getVisitor() {
		return visitor;
	}

	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Date getDateOfExit() {
		return dateOfExit;
	}

	public void setDateOfExit(Date dateOfExit) {
		this.dateOfExit = dateOfExit;
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public Date getDateOfBadgeReceive() {
		return dateOfBadgeReceive;
	}

	public void setDateOfBadgeReceive(Date dateOfBadgeReceive) {
		this.dateOfBadgeReceive = dateOfBadgeReceive;
	}
	
	public Visit() {
		
	}

	public Visit(Long visitId, Visitor visitor, Employee employee, Date arrivalDate, Date arrivalTime, Date dateOfExit,
			String badgeNumber, Date dateOfBadgeReceive) {
		super();
		this.visitId = visitId;
		this.visitor = visitor;
		this.employee = employee;
		this.arrivalDate = arrivalDate;
		this.arrivalTime = arrivalTime;
		this.dateOfExit = dateOfExit;
		this.badgeNumber = badgeNumber;
		this.dateOfBadgeReceive = dateOfBadgeReceive;
	}

	@Override
	public boolean isNew() {
		return this.visitId== null;
	}

	@Override
	public Long getId() {
		return visitId;
	}
}