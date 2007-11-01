<?xml version='1.0'?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>



	<xsl:template match="details">
		<fieldset id="details">
			<legend>Details</legend>
			<xsl:variable name="countdays"><xsl:value-of select="count(/report/data/details/hours/hour)"/></xsl:variable>
			
			<h1>Totals for period per user</h1>
			<hr/>
			<table style="">
				<thead>
					<tr>
						<td width="150">Name</td>
						<td width="50">Type</td>
						<xsl:for-each select="/report/data/details/hours/hour">		
							<td width="40"><xsl:value-of select="."/>h</td>
						</xsl:for-each>	

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
							<td rowspan="2" class="{$colour}"><xsl:value-of select="name"/></td>
							<td class="{$td_natural}" width="40">hits</td>
							<xsl:for-each select="/report/data/details/hours/hour">	
								<xsl:variable name="curh"><xsl:value-of select="."/></xsl:variable>
								<xsl:variable name="requests"><xsl:value-of select="sum(/report/data/details/usagedetails[@userid = $userid]/dailyUsage/hourlyUsage[@hour=$curh]/@requests)"/></xsl:variable>
								<td class="{$td_natural}" width="40"><xsl:value-of select="$requests"/></td>
							</xsl:for-each>	
						</tr>
						<tr>
							<td class="{$td_variance}" width="40">data</td>
							<xsl:for-each select="/report/data/details/hours/hour">	
								<xsl:variable name="curh"><xsl:value-of select="."/></xsl:variable>
								<xsl:variable name="dataUsageBytes"><xsl:value-of select="sum(/report/data/details/usagedetails[@userid = $userid]/dailyUsage/hourlyUsage[@hour=$curh]/@dataUsage)"/></xsl:variable>
								<td class="{$td_variance}" width="40"><xsl:value-of select="format-number($dataUsageBytes * $bToMb,$dispMb)"/></td>
							</xsl:for-each>	
						
						</tr>
					</xsl:for-each>
				</tbody>
				
				<xsl:if test="count(/report/vars/users/user) > 1">
					<tfoot>
						<tr>
							<td rowspan="2">Totals</td>
							<td>hits</td>
							<xsl:for-each select="/report/data/details/hours/hour">	
								<xsl:variable name="curh"><xsl:value-of select="."/></xsl:variable>
								<xsl:variable name="requests"><xsl:value-of select="sum(/report/data/details/usagedetails/dailyUsage/hourlyUsage[@hour=$curh]/@requests)"/></xsl:variable>
								<td width="40"><xsl:value-of select="$requests"/></td>
							</xsl:for-each>	
						</tr>
						<tr>
							<td>data</td>
							<xsl:for-each select="/report/data/details/hours/hour">	
								<xsl:variable name="curh"><xsl:value-of select="."/></xsl:variable>
								<xsl:variable name="dataUsageBytes"><xsl:value-of select="sum(/report/data/details/usagedetails/dailyUsage/hourlyUsage[@hour=$curh]/@dataUsage)"/></xsl:variable>
								<td width="40"><xsl:value-of select="format-number($dataUsageBytes * $bToMb,$dispMb)"/></td>
							</xsl:for-each>	

						</tr>

					</tfoot>
				</xsl:if>
			</table>
			
		</fieldset>
	</xsl:template>

</xsl:stylesheet>

