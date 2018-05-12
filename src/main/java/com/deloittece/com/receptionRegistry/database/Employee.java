package com.deloittece.com.receptionRegistry.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "employees")
@EntityListeners(AuditingEntityListener.class)
public class Employee {

	// for LDAP
	// private String cn;
	//
	//
	// public String getCn() {
	// return cn;
	// }
	//
	// public void setCn(String cn) {
	// this.cn = cn;
	// }

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EMPLOYEE_ID")
	private Long empId;

	@Column(name = "EMP_FULL_NAME")
	private String empFullName;

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getEmpFullName() {
		return empFullName;
	}

	public void setEmpFullName(String empFullName) {
		this.empFullName = empFullName;
	}

	public Employee(Long empId, String empFullName) {
		super();
		this.empId = empId;
		this.empFullName = empFullName;
	}

	public Employee() {

	}
}
