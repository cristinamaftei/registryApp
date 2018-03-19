package com.deloittece.com.receptionRegistry.database;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitRepository extends CrudRepository<Visit, Long>{
	
	@Modifying
	@Transactional
    @Query("UPDATE Visit v  SET v.dateOfExit= CURDATE()  WHERE arrivalDate < :date")
	void updateExitDate(@Param("date") java.sql.Date date);
	

}
