package com.deloittece.com.receptionRegistry.database;

//@Repository
public class VisitorSearch {

//	@Autowired
//	private EntityManager entityManager;
//
//	@Transactional
//	public void indexVisitors() {
//		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//		try {
//			fullTextEntityManager.createIndexer().startAndWait();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Transactional
//	public List<Visitor> searchVisitor(String icInfo) {
//		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
//
//		QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
//				.forEntity(Visitor.class).get();
//
//		org.apache.lucene.search.Query query = queryBuilder.keyword().onField("icInfo").matching(icInfo).createQuery();
//
//		org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query,
//				Visitor.class);
//
//		List<Visitor> results = jpaQuery.getResultList();
//		return results;
//	}

}
