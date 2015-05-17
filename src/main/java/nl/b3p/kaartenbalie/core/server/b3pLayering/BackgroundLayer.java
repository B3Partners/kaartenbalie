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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import nl.b3p.ogc.utils.OGCRequest;

/**
 *
 * @author Chris Kramer
 */
public abstract class BackgroundLayer extends ConfigLayer {

    /** Creates a new instance of BackgroundLayer */
    public BackgroundLayer(String name, String title) {
        super(name, title);
    }

    /* Override */
    public BufferedImage imageHandler(OGCRequest ogcrequest, Map parameterMap, boolean transparant) throws Exception {
        BufferedImage bufImage = createBaseImage(ogcrequest, parameterMap);
        Graphics2D g2d = (Graphics2D) bufImage.getGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(20, 20, bufImage.getWidth() - 40, bufImage.getHeight() - 40);
        addTagsToImage(bufImage, parameterMap);
        return modifyBaseImage(bufImage, parameterMap);
    }
}
