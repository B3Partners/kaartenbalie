/*
 * KeywordList.java
 *
 * Created on 26 september 2006, 15:41
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
public class KeywordList {
    
    private ArrayList keywords = new ArrayList();
    
    /** Creates a new instance of KeywordList */
    public KeywordList() {
    }

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    public ArrayList getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList keywords) {
        this.keywords = keywords;
    }
    
}
