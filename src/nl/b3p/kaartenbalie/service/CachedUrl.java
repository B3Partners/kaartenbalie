/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.b3p.kaartenbalie.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

/**
 * B3partners B.V. http://www.b3partners.nl
 * @author Roy
 * Created on 30-aug-2011, 16:43:01
 */
public class CachedUrl extends Thread{

    public static int CREATED = 0;
    public static int LOADING = 1;
    public static int FINISHED = 2;
    public static int ERRORED = 9;
    
    private Date creationDate;
    private String url;
    private String content;
    private int status;
    private String charSet="UTF-8";
    private Object monitor;
    
    public CachedUrl(String url,Object monitor){
        this.status=CREATED;
        this.url=url;
        this.monitor=monitor;
    }

    @Override
    public void run() {
        try{
            URL theUrl = new URL(url);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            BufferedReader br
        	= new BufferedReader(
        		new InputStreamReader(theUrl.openStream(),charSet));
            StringBuilder sb = new StringBuilder();
 
            String line;
            while ((line = br.readLine()) != null) {
                    sb.append(line);
            } 
            content=sb.toString();            
            //cache is renewed at:
            status=FINISHED;            
        }catch(Exception e){
            this.status=ERRORED;
            content=e.toString()+": "+e.getMessage();
        }finally{            
            this.creationDate=new Date();
            System.out.println("Notify all!");
            synchronized(monitor){
                monitor.notifyAll();
            }
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="getters setters">
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    //</editor-fold>
    
}
