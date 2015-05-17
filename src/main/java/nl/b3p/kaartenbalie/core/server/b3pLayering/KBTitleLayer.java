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
import java.math.BigDecimal;
import java.util.Map;
import nl.b3p.ogc.utils.KBConfiguration;

/**
 *
 * @author Chris Kramer
 */
public class KBTitleLayer extends ConfigLayer {

    public static final String NAME = "kaartenbalie";
    public static final String TITLE = "Creditinfo";

    public KBTitleLayer() {
        super(NAME, TITLE);
    }

    public void processConfig(Map configMap) throws Exception {
    }

    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {

        StringBuffer message = new StringBuffer();

        BigDecimal cm = (BigDecimal) parameterMap.get(AllowTransactionsLayer.creditMutation);
        if (cm != null) {
            message.append(" (");
            message.append(KBConfiguration.MESSAGE_TITLE_CREDITS);
            message.append(cm.toString());
            message.append(KBConfiguration.MESSAGE_TITLE_UNIT);
        }

        Double balance = (Double) parameterMap.get(BalanceLayer.creditBalance);
        if (balance != null) {
            if (message.length() == 0) {
                message.append(" (");
            } else {
                message.append(", ");
            }
            message.append(KBConfiguration.MESSAGE_TITLE_BALANCE);
            message.append(balance.toString());
            message.append(KBConfiguration.MESSAGE_TITLE_UNIT);
        }
        if (message.length() > 0) {
            message.append(")");
        }
        message.insert(0, KBConfiguration.MESSAGE_TITLE_LAYER);

        Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
        int padding = 20;
        int w = bufImage.getWidth();
        int h = 19;
        int x = 0;
        int y = bufImage.getHeight() - h - padding;
        drawMessageBox(g2, message.toString(), x, y, w, h);
        return bufImage;
    }
}
