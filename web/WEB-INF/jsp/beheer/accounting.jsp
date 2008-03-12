<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<script type="text/javascript" src="<html:rewrite page="/js/niftycube.js" module="" />"></script>
<link rel="stylesheet" type="text/css" href="<html:rewrite page="/styles/niftyCorners.css" module="" />">

<H1>Beheer Accounting</H1>

<div class="tabcollection" id="accountCollection" style="margin-bottom: 15px;">
    <div id="tabs">
        <ul id="tabul" style="width: 650px;">
            <li id="AccountDetails" onclick="displayTabBySource(this);"><a href="#" style="width: 200px;">Account Details</a></li>
            <li id="Deposits" onclick="displayTabBySource(this);"><a href="#" style="width: 200px;">Laatste 20 Bijboekingen</a></li>
            <li id="Withdrawls" onclick="displayTabBySource(this);"><a href="#" style="width: 200px;">Laatste 20 Afboekingen</a></li>            
        </ul>
    </div>
    <script type="text/javascript">Nifty("ul#tabul a","medium transparent top");</script>
    <div id="sheets" style="height:500px;">
        <div id="AccountDetails" class="sheet">
            <label>Credit Balance :</label>
            <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${balance}" /> credits
            <p>
                <button onclick="location.href='deposit.do'">Credits aanschaffen</button>
            </p>
        </div>
        <div id="Deposits" class="sheet">
            <div style="padding-right: 20px;">
                <table id="depositTable" style="width: 100%; padding:0px; margin:0px; border-collapse: collapse; margin-left: 10px;" class="table-stripeclass:table_alternate_tr">
                    <thead>
                        <tr class="headerRijTitel">
                            <th>Aangemaakt</th>
                            <th>Verwerkt</th>
                            <th>Methode</th>
                            <th style="padding-right: 10px;">Valuta &euro;</th>
                            <th style="padding-right: 10px;">Koers</th>
                            <th style="padding-right: 10px;">Resultaat</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tpd" items="${paymentDeposits}">
                            <tr>
                                <td>
                                    <a href="#" onclick="parent.showPopup(800,600,'Transactie Details','transaction.do?transaction=submit&id=${tpd.id}');">
                                        <fmt:formatDate  value="${tpd.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                    </a>
                                </td>
                                <td>
                                    <fmt:formatDate  value="${tpd.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                </td>
                                <td>
                                    iDeal
                                </td>
                                <td style="text-align: right; padding-right: 10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tpd.billingAmount}" />
                                </td>
                                <td style="text-align: right; padding-right: 10px;" class="">
                                    1:${tpd.txExchangeRate}
                                </td>                    
                                <td style="text-align: right; padding-right: 10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tpd.creditAlteration}" /> c
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${tpd.status == 0}">
                                            Wachtrij
                                        </c:when>
                                        <c:when test="${tpd.status == 1}">
                                            Verwerkt
                                        </c:when>
                                        <c:when test="${tpd.status == 2}">
                                            Geweigerd
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>   
                    </tbody>
                </table>
                <script type="text/javascript">
                    Table.stripe(document.getElementById('depositTable'), 'table_alternate_tr');
                </script>
            </div>
        </div>
        <div id="Withdrawls" class="sheet">
            <div style="padding-right: 20px;">
                <table id="withdrawlTable" style="width: 100%; padding:0px; margin:0px; border-collapse: collapse; margin-left: 10px;" class="table-stripeclass:table_alternate_tr">
                    <thead>
                        <tr class="headerRijTitel">
                            <th>Aangemaakt</th>
                            <th>Verwerkt</th>
                            <th>&nbsp;</th>
                            <th style="padding-right: 10px;">Credits</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tlu" items="${layerUsages}">
                            <tr>
                                <td>
                                    <a href="#" onclick="parent.showPopup(800,600,'Transactie Details','transaction.do?transaction=submit&id=${tlu.id}');">
                                        <fmt:formatDate  value="${tlu.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                    </a>
                                </td>
                                <td>
                                    <fmt:formatDate  value="${tlu.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                </td>
                                <td>&nbsp;</td>
                                <td style="text-align: right; padding-right: 10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tlu.creditAlteration}"/> c
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${tlu.status == 0}">
                                            Wachtrij
                                        </c:when>
                                        <c:when test="${tlu.status == 1}">
                                            Verwerkt
                                        </c:when>
                                        <c:when test="${tlu.status == 2}">
                                            Geweigerd
                                        </c:when>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <script type="text/javascript">
                    Table.stripe(document.getElementById('withdrawlTable'), 'table_alternate_tr');
                </script>
            </div>
        </div>
    </div>
</div>
<script language="JavaScript" type="text/javascript">
        window.onLoad = registerCollection('accountCollection', 'AccountDetails');
</script>