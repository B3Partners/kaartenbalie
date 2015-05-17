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

public class AllowTransactionsLayer extends ConfigLayer {

    public static final String NAME = "allowwithdrawals";
    public static final String TITLE = "Afboekingen toestaan";
    public static final String allowTransactions = "allowTransactions";
    public static final String transactionsNeeded = "transactionsNeeded";
    public static final String creditMutation = "creditMutation";
    public static final String pricedLayers = "pricedLayers";
    public static final String foundAllowTransactionsLayer = "foundAllowTransactionsLayer";

    public AllowTransactionsLayer() {
        super(NAME, TITLE);
    }

    public void processConfig(Map configMap) throws Exception {
        configMap.put(allowTransactions, new Boolean(true));
        configMap.put(transactionsNeeded, new Boolean(false));
    }

    protected BufferedImage modifyBaseImage(BufferedImage bufImage, Map parameterMap) throws Exception {
        Boolean bAllowTransactions = (Boolean) parameterMap.get(allowTransactions);
        Boolean bTransactionsNeeded = (Boolean) parameterMap.get(transactionsNeeded);
        if ((bAllowTransactions == null || bAllowTransactions.booleanValue() == false) &&
                bTransactionsNeeded != null && bTransactionsNeeded.booleanValue() == true) {
            // transacties zijn niet toegestaan maar wel nodig dan toon waarschuwing
            int alignCenter = (bufImage.getWidth() / 2);
            int alignMiddle = (bufImage.getHeight() / 2);
            Graphics2D g2 = (Graphics2D) bufImage.getGraphics();
            int boxWidth = 400;
            int boxHeight = 150;

            StringBuffer message = new StringBuffer();
            message.append(KBConfiguration.MESSAGE_AUTHORIZATION_INTRO);
            message.append(KBConfiguration.MESSAGE_AUTHORIZATION_START);
            message.append(TITLE);
            message.append(" (");
            message.append(NAME);
            message.append(")");
            message.append(KBConfiguration.MESSAGE_AUTHORIZATION_END);
            message.append(KBConfiguration.MESSAGE_NO_DISPLAY_AGAIN);
            message.append(KBConfiguration.MESSAGE_REQUIRED_CREDITS);

            BigDecimal cm = (BigDecimal) parameterMap.get(creditMutation);
            String cmt = "n/a";
            if (cm != null) {
                cmt = cm.toString();
            }
            message.append(cmt);

            String pln = (String) parameterMap.get(pricedLayers);
            if (pln != null && pln.length() > 0) {
                message.append(" (");
                message.append(pln);
                message.append(")");
            }

            drawTitledMessageBox(g2,
                    TITLE, message.toString(),
                    alignCenter - (boxWidth / 2),
                    alignMiddle - (boxHeight / 2),
                    boxWidth, boxHeight);
        }
        return bufImage;
    }
}
