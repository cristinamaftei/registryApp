package com.deloittece.com.receptionRegistry.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//@Transactional
@Repository
public interface VisitorRepository extends CrudRepository<Visitor, Long>{
	
	Visitor findByIdentityCardInfo(String identityCardInfo);

	
}
