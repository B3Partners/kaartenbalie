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
public class BalanceLayer extends ConfigLayer {

    public static final String NAME = "creditbalance";
    public static final String TITLE = "Credittegoed";
    public static final String creditBalance = "balance";

    public BalanceLayer() {
        super(NAME, TITLE);
    }

    public void processConfig(Map configMap) throws Exception {
    }

    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {

        Double balance = (Double) parameterMap.get(creditBalance);
        String message = "n/a";
        if (balance != null) {
            message = balance.toString();
        }
        Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
        int padding = 20;
        int w = 120;
        int h = 18;
        int x = bufImage.getWidth() - w - padding;
        int y = padding;
        drawTitledMessageBox(g2, "Balance", message + " c", x, y, w, h);



        return bufImage;
    }
}
