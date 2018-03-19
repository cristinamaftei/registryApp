package com.deloittece.com.receptionRegistry.database;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//@Transactional
@Repository
public interface VisitorRepository extends CrudRepository<Visitor, Long>{
	
	Visitor findByIdentityCardInfo(String identityCardInfo);

	
}
