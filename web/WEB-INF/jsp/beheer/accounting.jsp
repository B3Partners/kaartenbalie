<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<div class="containerdiv" style=";">
    <H1>Beheer Accounting</H1>
    
    
    <div class="tabcollection" id="accountCollection">
        <div id="tabs">
            <ul>
                <li id="AccountDetails" onclick="displayTabBySource(this);">Account Details</li>
                <li id="Deposits"  onclick="displayTabBySource(this);">Laatste 20 Bijboekingen</li>
                <li id="Withdrawls"  onclick="displayTabBySource(this);">Laatste 20 Afboekingen</li>            
            </ul>
        </div>
        <div id="sheets" style="height:500px;">
            <div id="AccountDetails" class="sheet">
                <label>Credit Balance :</label>
                <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${balance}" /> credits
                <p>
                    <button onclick="location.href='deposit.do'">Credits aanschaffen</button>
                </p>
            </div>
            <div id="Deposits" class="sheet">
                <table>
                    <thead>
                        <tr>
                            <th style="width: 120px;">Aangemaakt</th>
                            <th style="width: 140px;">Verwerkt</th>
                            <th style="width: 75px;">Methode</th>
                            <th style="width: 60px;text-align:right;padding-right:10px;">Valuta &euro;</th>
                            <th style="width: 60px;text-align:right;padding-right:10px;">Koers</th>
                            <th style="width: 110px;text-align:right;padding-right:10px;">Resultaat</th>
                            <th style="width: 70px;">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tpd" items="${paymentDeposits}">
                            <tr>
                                <td style="width: 120px;" class="">
                                    <a href="#" onclick="parent.showPopup(800,600,'Transactie Details','transaction.do?transaction=submit&id=${tpd.id}');">
                                        <fmt:formatDate  value="${tpd.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                    </a>
                                </td>
                                <td style="width: 140px;" class="">
                                    <fmt:formatDate  value="${tpd.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                </td>
                                <td style="width: 75px;" class="">
                                    iDeal
                                </td>
                                <td style="width: 60px;text-align:right;padding-right:10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tpd.billingAmount}" />
                                </td>
                                <td style="width: 60px;text-align:right;padding-right:10px;" class="">
                                    1:${tpd.txExchangeRate}
                                </td>                    
                                <td style="width: 110px;text-align:right;padding-right:10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tpd.creditAlteration}" /> c
                                </td>
                                <td style="width: 70px;" class="">
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
            </div>
            <div id="Withdrawls" class="sheet">
                <table>
                    <thead>
                        <tr>
                            <th style="width: 120px;">Aangemaakt</th>
                            <th style="width: 140px;">Verwerkt</th>
                            <th style="width: 220px;">&nbsp;</th>
                            <th style="width: 110px;text-align:right;padding-right:10px;">Credits</th>
                            <th style="width: 70px;">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="tlu" items="${layerUsages}">
                            <tr>
                                <td style="width: 120px;" class="">
                                    <a href="#" onclick="parent.showPopup(800,600,'Transactie Details','transaction.do?transaction=submit&id=${tlu.id}');">
                                        <fmt:formatDate  value="${tlu.transactionDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                    </a>
                                </td>
                                <td style="width: 140px;" class="">
                                    <fmt:formatDate  value="${tlu.mutationDate}" pattern="dd-MM-yyyy @ HH:mm"/>
                                </td>
                                <td style="width: 220px;">&nbsp;</td>
                                <td style="width: 110px;text-align:right;padding-right:10px;" class="">
                                    <fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" value="${tlu.creditAlteration}"/> c
                                </td>
                                <td style="width: 70px;" class="">
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
            </div>
        </div>
    </div>
        <script language="JavaScript" type="text/javascript">
        window.onLoad = registerCollection('accountCollection', 'AccountDetails');
    </script>
    
    
    
    
    
    
    
</div>