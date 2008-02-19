<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	
	<xsl:template match="data">
		<fieldset id="data">
			<legend>Results</legend>
			<xsl:apply-templates select="details"/>
                        <xsl:for-each select="/report/dataTable">
                            <h1><xsl:value-of select="@tableName"/></h1>
                            <hr/>
                            <table style="" id="@tableName">
                            <xsl:for-each select="tableRow">        
                                <xsl:choose>
                                        <xsl:when test="@header = 'true'">
                                            <thead>
                                                <tr>
                                                    <xsl:for-each select="rowValue">  
                                                        <td><xsl:value-of select="."/></td> 
                                                    </xsl:for-each>
                                                </tr>
                                            </thead>
                                        </xsl:when>
                                        <xsl:otherwise>
                                            <tbody>
                                                <tr>
                                                    <xsl:for-each select="rowValue">  
                                                        <td><xsl:value-of select="."/></td>
                                                    </xsl:for-each>
                                                </tr>
                                            </tbody>
                                        </xsl:otherwise>
                                </xsl:choose>
                            </xsl:for-each>
                            </table>
                        </xsl:for-each>
                </fieldset>
                
	</xsl:template>
	

	<xsl:include href="data_details.xsl"/>
</xsl:stylesheet>



