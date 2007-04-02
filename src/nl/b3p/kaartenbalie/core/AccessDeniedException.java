/*
 * @(#)AccessDeniedException.java
 * @author N. de Goeij
 * @version 1.00, 30 maart 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.core;

/**
 * AccessDeniedException definition:
 *
 */

public class AccessDeniedException extends Exception {
    
    /** Creates a new instance of AccessDeniedException */
    public AccessDeniedException() {
        super();
    }
    
    public AccessDeniedException(String errorMessage) {
        super(errorMessage);
    }
}
