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
package nl.b3p.kaartenbalie.service.requesthandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;
import nl.b3p.ogc.utils.OGCConstants;
import nl.b3p.ogc.utils.OGCRequest;

public class TextToImage {

    private String imageType;

    public void createImage(String message, DataWrapper data) throws IOException {
        this.imageType = imageType;
        Color background = Color.white;
        Color textColor = Color.black;
        Font font = new Font("Serif", Font.PLAIN, 14);

        BufferedImage buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = buffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontRenderContext fc = g2.getFontRenderContext();

        OGCRequest ogcrequest = data.getOgcrequest();

        // calculate the size of the text
        int width = Integer.parseInt(ogcrequest.getParameter(OGCConstants.WMS_PARAM_WIDTH));
        int height = Integer.parseInt(ogcrequest.getParameter(OGCConstants.WMS_PARAM_HEIGHT));

        //Hack: set width and height to something sensible
        if (width <= 0) {
            width = 200;
        }
        if (height <= 0) {
            height = 200;        
        }
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
        String requestedImageType = ogcrequest.getParameter(OGCConstants.WMS_PARAM_FORMAT);
        if (requestedImageType.equalsIgnoreCase("image/jpeg")) {
            this.imageType = "JPEG";
        } else if (requestedImageType.equalsIgnoreCase("image/png")) {
            this.imageType = "PNG";
        } else if (requestedImageType.equalsIgnoreCase("image/gif")) {
            this.imageType = "GIF";
        }

        ImageIO.write(buffer, imageType, baos);
        data.write(baos);
    }

    private ArrayList cutMessage(String message, int width, int height) {
        ArrayList sentences = new ArrayList();
        if (message != null) {
            int length = message.length();
            int divide = (width / 7);
            int parts = length / divide;
            int leftOver = length - (parts * divide);

            for (int part = 0; part <= parts; part++) {
                int begin = part * divide;
                int end = part * divide + divide;
                if (part == parts) {
                    end = part * divide + leftOver;
                }
                String curString = message.substring(begin, end);
                sentences.add(curString);
            }
        }
        return sentences;
    }
}
