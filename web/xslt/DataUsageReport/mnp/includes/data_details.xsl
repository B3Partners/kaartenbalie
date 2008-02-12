<?xml version='1.0'?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>



	<xsl:template match="details">
            <h1>Detailoverzicht</h1>
            <xsl:variable name="countdays"><xsl:value-of select="count(/report/data/details/hours/hour)"/></xsl:variable>

            <hr/>
            <table style="" id="details">
                    <thead>
                        <tr>
                            <td> </td>
                            <td colspan="3">Datagebruik in MB</td>
                            <td colspan="3">Aantal opgevraagd</td>
                        </tr>
                        <tr>
                            <td width="150">Gebruiker</td>
                            <td>Ochtend</td>
                            <td>Middag</td>
                            <td width="60">Totaal</td>
                            <td>Ochtend</td>
                            <td>Middag</td>
                            <td width="60">Totaal</td>
                        </tr>
                    </thead>
                    <tbody>
                            <xsl:for-each select="/report/vars/users/user">
                                    <xsl:variable name="userid"><xsl:value-of select="@id"/></xsl:variable>
                                    <xsl:variable name="colour">
                                            <xsl:choose>
                                                    <xsl:when test="position() mod 2 ">
                                                            <xsl:value-of select="$td_natural"/>
                                                    </xsl:when>
                                                    <xsl:otherwise>
                                                            <xsl:value-of select="$td_variance"/>
                                                    </xsl:otherwise>
                                            </xsl:choose>
                                    </xsl:variable>
                                    <tr>
                                           
                                        <td rowspan="1" class="{$colour}"><b><xsl:value-of select="name"/></b></td>
                                        
                                        <xsl:variable name="dubOchtend"><xsl:value-of select="sum(/report/data/details/usagedetails[@userid = $userid]/dailyUsage/hourlyUsage[@hour &gt;= 1 and @hour &lt; 13]/@dataUsage)"/></xsl:variable>
                                        <td class="{$colour}">
                                            <xsl:value-of select="format-number($dubOchtend * $bToMb,$dispMb)"/>
                                        </td> 
                                        <xsl:variable name="dubMiddag"><xsl:value-of select="sum(/report/data/details/usagedetails[@userid = $userid]/dailyUsage/hourlyUsage[@hour &gt;= 13 or @hour &lt; 1]/@dataUsage)"/></xsl:variable>
                                        <td class="{$colour}">
                                            <xsl:value-of select="format-number($dubMiddag * $bToMb,$dispMb)"/>
                                        </td> 
                                        <xsl:variable name="dubTotaal"><xsl:value-of select="sum(/report/data/details/usagedetails[@userid = $userid]/dailyUsage/hourlyUsage/@dataUsage)"/></xsl:variable>
                                        <td class="{$colour}"><xsl:value-of select="format-number($dubTotaal * $bToMb,$dispMb)"/></td> 

                                        
                                        <td class="{$colour}">  
                                            <xsl:value-of select="sum(/report/data/details/usagedetails[@userid = $userid]/dailyUsage/hourlyUsage[@hour &gt;= 1 and @hour &lt; 13]/@requests)"/>
                                        </td> 
                                        <td class="{$colour}">
                                            <xsl:value-of select="sum(/report/data/details/usagedetails[@userid = $userid]/dailyUsage/hourlyUsage[@hour &gt;= 13 or @hour &lt; 1]/@requests)"/>
                                        </td> 
                                        <td class="{$colour}"><xsl:value-of select="sum(/report/data/details/usagedetails[@userid = $userid]/dailyUsage/hourlyUsage/@requests)"/></td> 
                                        
                                    </tr>

                            </xsl:for-each>
                    </tbody>

                    <xsl:if test="count(/report/vars/users/user) > 1">
                            <tfoot>
                                    <tr>
                                            <td rowspan="1">Totalen</td>
                                            <xsl:variable name="dubOchtend"><xsl:value-of select="sum(/report/data/details/usagedetails/dailyUsage/hourlyUsage[@hour &gt;= 1 and @hour &lt; 13]/@dataUsage)"/></xsl:variable>
                                            <td>
                                                <xsl:value-of select="format-number($dubOchtend * $bToMb,$dispMb)"/>
                                            </td> 
                                            <xsl:variable name="dubMiddag"><xsl:value-of select="sum(/report/data/details/usagedetails/dailyUsage/hourlyUsage[@hour &gt;= 13  or @hour &lt; 1]/@dataUsage)"/></xsl:variable>
                                            <td>
                                                <xsl:value-of select="format-number($dubMiddag * $bToMb,$dispMb)"/>
                                            </td> 
                                            <xsl:variable name="dubTotaal"><xsl:value-of select="sum(/report/data/details/usagedetails/dailyUsage/hourlyUsage/@dataUsage)"/></xsl:variable>
                                            <td><xsl:value-of select="format-number($dubTotaal * $bToMb,$dispMb)"/></td> 


                                            <td><xsl:value-of select="sum(/report/data/details/usagedetails/dailyUsage/hourlyUsage[@hour &gt;= 1 and @hour &lt; 13]/@requests)"/></td>
                                            <td><xsl:value-of select="sum(/report/data/details/usagedetails/dailyUsage/hourlyUsage[@hour &gt;= 13 or @hour &lt; 1]/@requests)"/></td>
                                            <td><xsl:value-of select="sum(/report/data/details/usagedetails/dailyUsage/hourlyUsage/@requests)"/></td>
                                    </tr>

                            </tfoot>
                    </xsl:if>
            </table>
	</xsl:template>

</xsl:stylesheet>

