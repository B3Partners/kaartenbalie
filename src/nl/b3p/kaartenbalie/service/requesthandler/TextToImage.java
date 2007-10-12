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
import java.util.Map;
import javax.imageio.ImageIO;
import nl.b3p.wms.capabilities.KBConstants;

public class TextToImage implements KBConstants {
    private String imageType;
    
    public void createImage(String message, String imageType, DataWrapper data) throws IOException {
        Map parameters = data.getParameters();        
        this.imageType = imageType;
        Color background = Color.white;
        Color textColor = Color.black;
        Font font = new Font("Serif", Font.PLAIN, 14);
        
        BufferedImage buffer = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext fc = g2.getFontRenderContext();
        
        // calculate the size of the text
        int width = Integer.parseInt((String)parameters.get(WMS_PARAM_WIDTH));
        int height = Integer.parseInt((String)parameters.get(WMS_PARAM_HEIGHT));

        // prepare some output
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(font);

        // actually do the drawing
        g2.setColor(background);
        g2.fillRect(0, 0, width, height);
        g2.setColor(textColor);
        
        ArrayList sentences = cutMessage(message, width, height);
        Iterator it = sentences.iterator();
        int x = 0;
        while (it.hasNext()) {
            String sentence = (String) it.next();
            g2.drawString(sentence, 0, x + 30);
            x += 15;
        }
                
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        // output the image as png
        if(this.imageType.equalsIgnoreCase("jpg")) {
            this.imageType = "JPEG";
        }
        
        ImageIO.write(buffer, imageType, baos);
        data.write(baos);
    }
    
    private ArrayList cutMessage(String message, int width, int height) {
        ArrayList sentences = new ArrayList();
        int length = message.length();
        int divide = (width / 7);
        int parts = length / divide;
        int leftOver = length - (parts* divide);
        
        for(int part = 0; part <= parts; part++) {
            int begin = part * divide;
            int end = part * divide + divide;
            if(part == parts) {
                end = part * divide + leftOver;
            }
            String curString = message.substring(begin, end);
            sentences.add(curString);
        }
        return sentences;
    }    
}
