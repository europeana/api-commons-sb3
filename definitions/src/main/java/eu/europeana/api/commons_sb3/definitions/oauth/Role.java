package eu.europeana.api.commons_sb3.definitions.oauth;

/**
 * This is an interface for user roles enumeration
 * 
 * @author GrafR
 *
 */
public interface Role {
    
    
    /**
     * @return the name of the role
     */
    public String getName();
    
    /**
     * @return the permissions for given role
     */
    public String[] getPermissions();

}