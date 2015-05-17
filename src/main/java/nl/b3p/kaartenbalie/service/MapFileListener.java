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


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import nl.b3p.kaartenbalie.core.server.Organization;
import nl.b3p.kaartenbalie.core.server.persistence.MyEMFDatabase;
import nl.b3p.kaartenbalie.struts.WmsServerAction;
import nl.b3p.wms.capabilities.ServiceProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
/**
 *
 * @author Meine Toonen meinetoonen@b3partners.nl
 */
public class MapFileListener implements FileListener {

    public Map<String, Integer> namesMap = new HashMap<String, Integer>();
    private Log log = LogFactory.getLog(this.getClass());
    private String organization;

    public MapFileListener(String organization) {
        this.organization = organization;
    }

    public void fileCreated(FileChangeEvent event) throws Exception {
        /*
         * haal wms_online_resource op creeër afkorting haal naam op --> roep
         * wmsaction aan met waardes
         */

        File mapPath = new File(event.getFile().getURL().getFile());
        if(mapPath.getName().indexOf(".map")!= -1 ){
            MapParser mapParser = new MapParser(mapPath);
            mapParser.parse();

            Map<String, String> metadata = mapParser.getWebMetadata();
            String url = metadata.get("wms_onlineresource");
            String mapfilename = mapPath.getName().substring(0, mapPath.getName().indexOf("."));
            mapfilename = mapfilename.replaceAll(" ", "_");

            saveServiceProvider(url, mapfilename);
        }
        event.getFile().close();
    }

    private void saveServiceProvider(String wmsUrl, String name) {
        Object identity = null;
        try {
            identity = MyEMFDatabase.createEntityManager(MyEMFDatabase.MAIN_EM);
            EntityManager em = MyEMFDatabase.getEntityManager(MyEMFDatabase.MAIN_EM);

            EntityTransaction tx = em.getTransaction();
            tx.begin();

            try {
                String getCap = "&service=WMS&request=GetCapabilities&version=1.1.1";

                Long number = getUniqueAbbr(name,em);
                String abbr = name + number ;

                ServiceProvider saveServiceProvider = WmsServerAction.saveServiceProvider(wmsUrl + getCap, null, name, abbr, em);
                Organization org = (Organization) em.createQuery("FROM Organization WHERE name = :name").setParameter("name", organization).getSingleResult();
                WmsServerAction.addAllLayersToGroup(org, saveServiceProvider, em);

                tx.commit();
            } catch (Exception ex) {
                tx.rollback();
                log.error("Kan nieuwe server niet posten", ex);
            }
        } catch (Throwable e) {
            log.error("Exception occured while getting EntityManager: ", e);
        } finally {
            log.debug("Closing entity manager .....");
            MyEMFDatabase.closeEntityManager(identity, MyEMFDatabase.MAIN_EM);
        }
    }

    private Long getUniqueAbbr(String name, EntityManager em){
        Query q = em.createQuery("SELECT COUNT(*) FROM ServiceProvider WHERE givenName like :name").setParameter("name",'%'+ name + '%');
        Long number = (Long)q.getSingleResult();
        number += 1;
        return number;
    }

    public void fileDeleted(FileChangeEvent event) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void fileChanged(FileChangeEvent event) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
