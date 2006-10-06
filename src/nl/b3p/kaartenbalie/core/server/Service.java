/*
 * Service.java
 *
 * Created on 18 september 2006, 11:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

import java.util.ArrayList;

/**
 *
 * @author Nando De Goeij
 */
public class Service {
    private String name;
    private String title;
    private String abstr;
    private String fees;
    private String constraints;
    private ArrayList keywordLists = new ArrayList();
    private ArrayList onlineResources = new ArrayList();
    private ContactInformation contactInformation;
    
    /** Creates a new instance of Service */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstr() {
        return abstr;
    }

    public void setAbstr(String abstr) {
        this.abstr = abstr;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }
    
    public ArrayList getOnlineResources() {
        return onlineResources;
    }

    public void setOnlineResources(ArrayList onlineResources) {
        this.onlineResources = onlineResources;
    }

    public void addOnlineResource(OnlineResource onlineResource) {
        onlineResources.add(onlineResource);
    }

    public void addKeywordList(KeywordList keywordList) {
        keywordLists.add(keywordList);
    }
    
    public void setKeywordLists(ArrayList keywordLists) {
        this.keywordLists = keywordLists;
    }
    
    public ArrayList getKeywordLists() {
        return keywordLists;
    }
    
    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }
}
