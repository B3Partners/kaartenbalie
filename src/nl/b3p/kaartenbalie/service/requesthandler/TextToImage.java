/*
 * @(#)TextToImage.java
 * @author N. de Goeij
 * @version 1.00, 4 april 2007
 *
 * @copyright 2007 B3Partners. All rights reserved.
 * B3Partners PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

package nl.b3p.kaartenbalie.service.requesthandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;

public class TextToImage {
    private String imageType;
    
    /** Creates a new instance of TextToImage */
    public TextToImage() {
    }
    
    public void createImage(String message, String imageType, DataWrapper data) throws IOException {
        this.imageType = imageType;
        Color background = Color.white;
        Color textColor = Color.black;
        Font font = new Font("Serif", Font.PLAIN, 14);
        
        BufferedImage buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext fc = g2.getFontRenderContext();
        
        Rectangle2D bounds = font.getStringBounds(message, fc);

        // calculate the size of the text
        int width = 450;//(int) bounds.getWidth();
        int height = 450;//(int) bounds.getHeight();

        // prepare some output
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(font);

        // actually do the drawing
        g2.setColor(background);
        g2.fillRect(0, 0, width, height);
        g2.setColor(textColor);
        
        ArrayList sentences = cutMessage(message);
        Iterator it = sentences.iterator();
        int x = 0;
        while (it.hasNext()) {
            String sentence = (String) it.next();
            g2.drawString(sentence, 0, x + 30);//(int)-bounds.getY());
            x += 15;
        }
                
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // output the image as png
        if(this.imageType.equalsIgnoreCase("jpg")) {
            this.imageType = "JPEG";
        }
        
        ImageIO.write(buffer, imageType, baos);
        data.write(baos);
        //return baos;
    }
    
    private ArrayList cutMessage(String message) {
        ArrayList sentences = new ArrayList();
        String temp = "";
        int length = message.length();
        for(int i = 0; i < length; i++) {
            char c = message.charAt(i);
            temp += c;
            if (i != 0) {
                if ((i % 70) == 0) {
                    sentences.add(temp);
                    temp = "";
                }
            }
        }
        if (sentences.size() == 0) {
            sentences.add(temp);
        }
        return sentences;
    }    
}
