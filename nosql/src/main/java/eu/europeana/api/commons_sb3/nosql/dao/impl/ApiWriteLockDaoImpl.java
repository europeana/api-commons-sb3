package eu.europeana.api.commons_sb3.nosql.dao.impl;

import java.io.Serializable;
import dev.morphia.Datastore;
import eu.europeana.api.commons_sb3.nosql.entity.ApiWriteLock;

public class ApiWriteLockDaoImpl <E extends ApiWriteLock, T extends Serializable>
extends NosqlDaoImpl<E, T>{

    public ApiWriteLockDaoImpl(Class<E> clazz, Datastore datastore) {
        super(datastore, clazz);
    }

}