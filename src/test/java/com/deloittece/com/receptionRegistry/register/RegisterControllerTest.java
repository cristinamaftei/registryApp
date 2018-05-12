package com.deloittece.com.receptionRegistry.register;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.deloittece.com.receptionRegistry.ReceptionRegistryApplication;
import com.deloittece.com.receptionRegistry.database.BadgeType;
import com.deloittece.com.receptionRegistry.database.Employee;
import com.deloittece.com.receptionRegistry.database.Visit;
import com.deloittece.com.receptionRegistry.database.VisitRepository;
import com.deloittece.com.receptionRegistry.database.Visitor;
import com.deloittece.com.receptionRegistry.database.VisitorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRunner.class)
// @WebMvcTest(value = RegisterController.class)

@SpringBootTest(classes = ReceptionRegistryApplication.class)
// webEnvironment = WebEnvironment.RANDOM_PORT

// @AutoConfigureMockMvc
// @TestPropertySource(
// locations = "classpath:application-integrationtest.properties")

public class RegisterControllerTest {

	@MockBean
	private RegisterController registerController;
	@MockBean
	private VisitorRepository visitorRepository;

	List<Employee> allEmployee;
	Employee employeeTest;
	Employee employeeTest2;
	Employee employeeTest3;

	@Before
	public void setUp() {
		Visitor visitorTest = new Visitor(1L, "Visitor Test", "test@yahoo.com", "AA123456");
		Visitor visitorTest2 = new Visitor(2L, "Visitor Test2", "test2@yahoo.com", "BB123456");
		employeeTest = new Employee(1L, "Employee Test");
		employeeTest2 = new Employee(2L, "Employee Test Secound");
		employeeTest3 = new Employee(3L, "Employee Third");
		Visit visitTest = new Visit(1L, visitorTest, employeeTest, "11111", false, BadgeType.TEMPORARILY_ADC);
		Visit visitTest2 = new Visit(2L, visitorTest2, employeeTest, "11112", false, BadgeType.VISITOR_ERDC);

		allEmployee = new ArrayList<>();
		allEmployee.add(employeeTest);
		allEmployee.add(employeeTest2);
		allEmployee.add(employeeTest3);

		List<Employee> employees = new ArrayList<>();
		employees.add(employeeTest);
		employees.add(employeeTest2);

		Mockito.when(registerController.getEmployees(employeeTest.getEmpFullName())).thenReturn(employees);
	}

	@Test
	public void whenValidEmployeeName_thenEmployeeShouldBeFound() {

		String testEmployeeName = "Employee Test";
		List<Employee> employeesListFilter = registerController.getEmployees(testEmployeeName);

		assertNotEquals(employeesListFilter, allEmployee);
		assertTrue(employeesListFilter.size() == 2);
		assertTrue(employeesListFilter.get(0).getEmpFullName().equals(testEmployeeName));
		assertTrue(employeesListFilter.get(1).getEmpFullName().contains(testEmployeeName));

	}

}
