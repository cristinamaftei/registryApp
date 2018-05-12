package com.deloittece.com.receptionRegistry.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>{
	
	Employee findByEmpFullName (String empFullName);

}

////for LDAP
//public class EmployeeRepository {
//
//	private LdapTemplate ldapTemplate;
//
//	public void setLdapTemplate(LdapTemplate ldapTemplate) {
//		this.ldapTemplate = ldapTemplate;
//	}
//
//	public List<Employee> getAllEmployee(Filter filter) {
//		return ldapTemplate.search("", filter.encode(), new AttributesMapper() {
//
//			@Override
//			public Employee mapFromAttributes(Attributes attr) throws NamingException {
//				Employee employee = new Employee();
//				employee.setCn((String) attr.get("cn").get());
//				return employee;
//			}
//		});
//	}
//}
