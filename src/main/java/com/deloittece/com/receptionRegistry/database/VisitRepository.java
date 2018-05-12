package com.deloittece.com.receptionRegistry.database;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long>{
	
	//if the visitor doesn't logout when he finished the visit, the date of exit will be set automatically after one week
	@Modifying
	@Transactional
    @Query("UPDATE Visit v  SET v.dateOfExit= CURDATE(), v.exitWasSetAuto = 1 WHERE arrivalDate < :date")
	void updateExitDate(@Param("date") java.sql.Date date);
	

}
