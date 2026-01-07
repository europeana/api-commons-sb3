package eu.europeana.api.commons_sb3.nosql.service;

import eu.europeana.api.commons_sb3.definitions.oauth.Operations;
import eu.europeana.api.commons_sb3.definitions.oauth.exception.ApiWriteLockException;
import eu.europeana.api.commons_sb3.nosql.entity.ApiWriteLock;

import java.util.Set;

/**
 * Checks the locks in the write db.
 *
 * @author Sristhti singh
 * @since 4 December 2025
 */
public class WriteLockAuthorizationService {

    private final ApiWriteLockService apiWriteLockService;

    public WriteLockAuthorizationService(ApiWriteLockService apiWriteLockService) {
        this.apiWriteLockService = apiWriteLockService;
    }

    public ApiWriteLockService getApiWriteLockService() {
        return apiWriteLockService;
    }

    //

    /**
     * Check if a write lock is in effect. Returns HttpStatus.LOCKED in case the write lock is active.
     * To be used for preventing access to the write operations when the application is locked Needs
     * to be called explicitly in the verifyWriteAccess methods of individual apis
     *
     * NOTE : to use this method you need to create  a bean for ApiWriteLockService
     *        aka eu.europeana.api.commons.nosql.service.impl.ApiWriteLockServiceImpl with dao ref
     *
     * @param operationName
     * @throws ApiWriteLockException
     */
    public void checkWriteLockInEffect(String operationName)
            throws ApiWriteLockException {
        ApiWriteLock activeWriteLock;
        activeWriteLock = getApiWriteLockService().getLastActiveLock(ApiWriteLock.LOCK_WRITE_TYPE);
        // refuse operation if a write lock is effective (allow only unlock and retrieve
        // operations)
        if (activeWriteLock == null) {
            // the application is not locked
            return;
        }
        if (!isMaintenanceOperation(operationName)) {
            // unlock operation should be permitted when the application is locked
            // activeWriteLock.getEnded()==null
            throw new ApiWriteLockException("Operation not permitted when application is locked");
        }
    }

    /**
     * Indicates if the given operation is allowed when locked for maintenance. Basically
     *
     * @param operationName
     * @return
     */
    protected boolean isMaintenanceOperation(String operationName) {
        return getMaintenanceOperations().contains(operationName);
    }

    /**
     * Returns the list of
     *
     * @return
     */
    protected Set<String> getMaintenanceOperations() {
        return Set.of(Operations.WRITE_UNLOCK);
    }


}
