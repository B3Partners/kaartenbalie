<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">





	<xsl:template match="vars">
		<xsl:apply-templates select="period"/>
		<xsl:apply-templates select="company"/>
		
		<xsl:apply-templates select="users"/>
	</xsl:template>

	<xsl:template match="period">
		<fieldset id="period">
			<legend>Report Period</legend>
			<label>start:</label><xsl:value-of select="start"/><br/>
			<label>end:</label><xsl:value-of select="end"/><br/>			
		</fieldset>

	</xsl:template>


	<xsl:template match="company">
		<fieldset id="company">
			<legend>Company</legend>
			<label>Id:</label><xsl:value-of select="@id"/><br/>
			<label>name:</label><xsl:value-of select="name"/><br/>			
		</fieldset>
	</xsl:template>
	<xsl:template match="users">
		<xsl:if test="count(user) > 0">
			<fieldset id="users">
				<legend>Users In Report</legend>
				<table border="0">
					<thead>
						<tr>
							<td>ID</td>
							<td>NAME</td>
						</tr>
					</thead>

				<xsl:for-each select="user">
					<tbody>
						<xsl:variable name="baseFontSize" select="8"/>
						<tr>
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
							<td class="{$colour}"><xsl:value-of select="@id" /></td>
							<td class="{$colour}"><xsl:value-of select="name"/></td>
						</tr>
					</tbody>
				</xsl:for-each>
				</table>
			</fieldset>
		</xsl:if>		
	</xsl:template>
	
	

</xsl:stylesheet>

