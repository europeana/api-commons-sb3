package eu.europeana.api.commons_sb.nosql.service;

import eu.europeana.api.commons_sb.definitions.oauth.exception.ApiWriteLockException;
import eu.europeana.api.commons_sb.nosql.entity.ApiWriteLock;

public interface ApiWriteLockService extends AbstractNoSqlService<ApiWriteLock, String> {

	ApiWriteLock lock(String lockType) throws ApiWriteLockException;
	
	void unlock(ApiWriteLock writeLock) throws ApiWriteLockException;
	
	ApiWriteLock getLastActiveLock(String lockType) throws ApiWriteLockException;
	
	ApiWriteLock getLockById(String id) throws ApiWriteLockException;

	void deleteAllLocks() throws ApiWriteLockException;
	
}