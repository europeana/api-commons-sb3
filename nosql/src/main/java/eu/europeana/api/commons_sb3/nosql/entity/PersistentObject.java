package eu.europeana.api.commons_sb3.nosql.entity;

import java.util.Date;

public interface PersistentObject {

    Date getCreated();

    void setCreated(Date creationDate);

    Date getLastUpdate();

    void setLastUpdate(Date lastUpdate);
}