/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.b3p.kaartenbalie.service;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;

/**
 *
 * @author Meine Toonen meinetoonen@b3partners.nl
 */
public class MapFileScanner implements ServletContextListener {
    private static final String PARAM_DIRECTORY = "MapFileScanner.scandirectory";
    private static final String PARAM_ORGANIZATION = "MapFileScanner.organization";

    private Log log = LogFactory.getLog(this.getClass());;
    private static String scandirectory;
    private static String organization;
    private ServletContext context;
    
    private FileSystemManager fsManager;
    private DefaultFileMonitor fm;
    private FileObject listendir;
    
    public void contextInitialized(ServletContextEvent sce) {
        try {
            this.context = sce.getServletContext();
            init();
            
            fsManager = VFS.getManager();
            
            listendir = fsManager.resolveFile(scandirectory);
            fm = new DefaultFileMonitor(new MapFileListener( organization));
            fm.setRecursive(true);
            fm.addFile(listendir);
            fm.start();
            
        } catch (FileSystemException ex) {
            log.error("Cannot initialize MapFileScanner: ",ex);
        }
    }
    
    private void init(){
        scandirectory = context.getInitParameter(PARAM_DIRECTORY);
        if (scandirectory == null || scandirectory.isEmpty()) {
            scandirectory =  MyEMFDatabase.getMapfiles();
        }
        organization = context.getInitParameter(PARAM_ORGANIZATION);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        if (fm!=null) {
            fm.stop();
        }
        if (fsManager!=null && listendir!=null) {
            fsManager.closeFileSystem(listendir.getFileSystem());
        }
    }
}
