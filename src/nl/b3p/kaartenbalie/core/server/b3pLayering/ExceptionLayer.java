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
package nl.b3p.kaartenbalie.core.server.b3pLayering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 *
 * @author Chris Kramer
 */
public class ExceptionLayer extends BackgroundLayer {

    public static final String NAME = "exceptionlayer";
    public static final String TITLE = "Kaartenbalie Foutbericht";

    public ExceptionLayer() {
        super(NAME, TITLE);
    }

    public void processConfig(Map configMap) throws Exception {
    }

    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {

        String message = (String) parameterMap.get("message");
        // positie in scherm
        Integer index = (Integer) parameterMap.get("index");
        int i = 0;
        if (index!=null) {
            i = index.intValue();
        }

        Graphics2D g2 = (Graphics2D) bufImage.getGraphics();

        int paddingsides = 30;
        int w = bufImage.getWidth() - (paddingsides * 2);
        int h = message.length()*120/w;
        if (message != null) {
            drawTitledMessageBox(g2,
                    TITLE, message,
                    paddingsides, 80 + (i*(h+10)), w, h);
        }
        return bufImage;
    }
}
