package com.deloittece.com.receptionRegistry.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>{
	
	Employee findByEmpFullName (String empFullName);
}
