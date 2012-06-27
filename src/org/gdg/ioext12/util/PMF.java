package org.gdg.ioext12.util;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
    
    /**
     * holding the {@link PersistenceManagerFactory}.
     */
    private static final PersistenceManagerFactory pmfInstance =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");

    /**
     * private constructor so no object is created.
     */
    private PMF() {}

    /**
     * public method which returns the {@link PersistenceManagerFactory}.
     * 
     * @return {@link PersistenceManagerFactory}
     */
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
