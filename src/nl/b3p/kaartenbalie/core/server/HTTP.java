/*
 * HTTP.java
 *
 * Created on 26 september 2006, 13:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nl.b3p.kaartenbalie.core.server;

/**
 *
 * @author Nando De Goeij
 */
public class HTTP {
    
    private Get get;
    private Post post;
    
    /** Creates a new instance of HTTP */
    public HTTP() {
    }
    
    public Get getGet() {
        return get;
    }

    public void setGet(Get get) {
        this.get = get;
    }
    
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
