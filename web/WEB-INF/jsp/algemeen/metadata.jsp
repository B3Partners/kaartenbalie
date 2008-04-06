<%@include file="/WEB-INF/jsp/taglibs.jsp" %>

<script type="text/javascript" src="<html:rewrite page='/js/simple_treeview.js' module='' />"></script>

<div class="containerdiv" style="height: 500px;">
    <h1>B3P Metadata Editor (metadatastandaard kernset voor geografie 1.1)</h1>
    
    <div class="mdContainerDiv">
        <div class="containerdivFloat">
            <p>
            U kunt deze metadata editor gratis gebruiken vanaf deze website.
            Nadat u de gegevens heeft ingevuld kunt u het xml-bestand naar u toe
            mailen.
            </p>
            <p>
            Deze metadata editor is conform metadatastandaard kernset voor geografie 1.1 en
            implementeert het Nederlandse profiel op ISO19115:2003/ ISO19139
            </p>
            <p>
            Een aantal velden hebben een vooringevulde waarde. Ook deze velden
            kunt u aanpassen.
            </p>
            <p>
            Indien u deze editor in uw eigen omgeving wilt toepassen, kunt u 
            contact opnemen met B3Partners BV: 
            <a href="mailto:info@b3partners.nl">info@b3partners.nl</a>
            </p>
        </div>
        <c:set var="link">
            <html:rewrite page="/editmetadata.do?edit=t"/>
        </c:set>
        <iframe src="${link}" frameborder="0" id="metadataIframe" name="metadataIframe" class="mdIframe" width="630" height="450"></iframe>
    </div>	
</div>


