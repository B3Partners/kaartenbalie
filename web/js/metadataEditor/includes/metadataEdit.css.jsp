<%@page contentType="text/css"%>
<%@include file="/templates/taglibs.jsp" %>

/*Auteur: Erik van de Pol. B3Partners.*/

body {
  background-color: #FBFFFF;
  margin: 0px;
  font-size: 12px;
  font-family: Arial, sans-serif;
}

p {
  font-size: normal;
  margin: 0px; 
  /*display: block;*/
}

a {
  text-decoration: none;
}

a:hover {
  background-color: transparent;
  color: black;
}

table {
  position: relative;
  top: 0px;
  vertical-align: top;
}

td {
  text-align: center;
  font-weight: bold;
  font-size: 11pt;
  border: 0px none;
  border-right: 1px solid White;
}	
	
h1		{
		margin: 0px;
		font-size: 16px;
		color: #ffffff;
		}
h2		{
		margin: 5px;
		font-size: 14px;
		}
ul		{
		margin: 0px;
		}
li		{
		margin-left: 50px;
		margin-bottom: 2px;
		}
textarea	{
		font-family: Arial, sans-serif;
		font-size: 12px;
		}
input	{
		font-family: Arial, sans-serif;
		font-size: 12px;
		}

/* pop-up menus */

.menu {
	background-color: #fff;
	border: 1px solid #999; 
	padding: 2px 0px 2px 0px;
	position: absolute;  /* key attribute for hovering menu */
	/*z-index: 9990;*/ /* werkt niet helemaal lekker in IE */
	display: none;
}
ul.menu a {
	color: #000;
	margin: 1px 0px 1px 0px;
	padding: 1px 5px 1px 10px;
	/*font-size: 12px;*/
	font-weight: normal;
	width: 180px;
	/*width: 100%;*/
}		
ul.menu a:hover {
	background-color: #eee;
	color: black;
}		
ul.menu li a {
	margin: 1px 0px 1px 0px;
	color: #000;
	padding: 1px 5px 1px 10px;
	font-weight: normal;
	width: 180px;
}		
ul.menu li a:hover {
	background-color: #eee;
	color: black;
}		
ul.menu li {
	margin: 0px;
	margin-left: 5px;
	margin-right: 5px;
}
	
/* span classes */
/*
.plus_minus	{
		color: #11b;
		font-family: monospace;
		font-size: 16px;
		font-weight: bold;
		text-decoration: none;
		margin: 0px 2px 0px 0px;
		}	
*/
.instruction {
		color: #000000;
		font-size: 12px;
		font-weight: normal;
		font-style: normal;
		padding-right: 5px;
		margin-left: 20px;
		}

.default_value {
		color: #2D7D32;
		cursor: hand;
		font-size: 12px;
		font-weight: normal;
		font-style: normal;
		}
.mandatory_value {
		color: #2D0000;
		cursor: hand;
		font-size: 12px;
		font-weight: bold;
		font-style: normal;
		}		

.repeat_element {
		font-weight: normal;
		/*margin-left:12px;*/
		margin-left: 39px;
		text-indent: -27px;
		}
.repeat_Belement {
		font-weight: bold;
		/*margin-left:12px;*/
		margin-left: 39px;
		text-indent: -27px;
		}
.repeat_Melement {
		font-weight: bold;
		color: #EE0000;
		margin-left: 39px;
		text-indent: -27px;
		}		
.hiddenXML	{
		display: none;
		}
.noedit	{
		color: #777;
		font-weight: normal;
}

.unchanged_value {
  cursor: pointer;
  cursor: hand;
  color: #000099;
  font-size: 12px;
  font-weight: normal;
  font-style: normal;
  padding-right: 2px;
}

.changed_value {
  color: purple;
  cursor: pointer;
  cursor: hand;
  font-size: 12px;
  font-weight: normal;
  font-style: normal;
  padding-right: 2px;
}

.single_element	{
  font-weight: normal;
  /*margin-left:12px;*/
  /*following two lines for hanging indent*/
  /*margin-left: 39px;
  text-indent: -27px;*/
  margin-left: 0px;
  /*text-indent: 0px;*/
}

.single_Belement {
  font-weight: bold;
  margin-left: 0px;
  /*text-indent: 0px;*/
}

.single_Melement {
  font-weight: bold;
  color: #EE0000;
  margin-left: 0px;
  /*text-indent: 0px;*/
}



.section_title {
		color: #000;
		font-size: 13px;
		text-decoration: none;
		font-weight: bold;
		padding-left: 2px;
		}
.section_Stitle {
		color: #000;
		font-size: 13px;
		text-decoration: none;
		font-weight: normal;
		padding-left: 2px;
		}
.section_Mtitle {
		/*color: #EE0000;*/
		color: #2E8B57;
		font-size: 13px;
		text-decoration: none;
		font-weight: bold;
		padding-left: 0px;
		}
.section_Mtitle_hover {
		color: #00DD00;
		font-size: 13px;
		text-decoration: none;
		font-weight: bold;
		padding-left: 0px;
		}
/*
.section_Ftitle {
		color: #000;
		font-size: 15px;
		text-decoration: none;
		font-weight: bold;
		padding-left: 2px;
		}		
*/		

.section_path {
		color: #999;
		font-size: 12px;
		text-decoration: none;
		/*font-weight: bold;*/
		padding-right: 2px;
		}
		

.expand {
	position: relative;
	left: -16px;
}

.menuimg {
	position: relative;
	left: -16px;
}


/* div sections */
div.header {
	background-color: #000000;
	margin: 0px;
	padding: 5px 5px 5px 5px;
	}
div.titlebar {
	background-color: #f5f0dd;
	margin: 0px;
	padding: 5px 5px 5px 5px;
	border-top: solid 1px #888888;
	border-bottom: solid 1px #888888;
	font-size: 14px;
	font-weight: bold;
	}
div.instruction {
	margin: 5px;
}
div.main {
	margin: 5px;	
}
div.footer {
	background-color: #f5f0dd;
	margin: 0px;
	padding: 5px;
	border-top: solid 1px #888888;
	border-bottom: solid 1px #888888;
}
div.hidden {
	display: none;
}

div.folder {
	border:0px solid #fff;
	margin:0px 0px 0px 0px;	
}

div.content	{
	margin: 0px;
	margin-left: 16px;
}

div.section {
	margin-top:2px;
	margin-bottom:2px;
}


/* buttons */
input.section {
   color: #676769;
   font-family: arial,helvetica,sans-serif;
   font-size: 80%;
   font-weight: bold;
   background-color: #f5f0dd;
   border: 1px solid #676769;
}
input.element {
   color: #676769;
   /*font-family:arial,helvetica,sans-serif;*/
   font-family: monospace;
   font-size: 70%;
   font-weight: bold;
   background-color: #f5f0dd;
   border: 1px solid #676769;
}


/* overig */

#main-menu {
  border: 0px none;
}

#properties-box {
  border: 2px solid #b0c900; 
  position: relative;
  top: 0px;
}

.tab-selected {
  color: #FFFFFF;
  background-color: #D6E481;
}

.tab-unselected {
  color: #FFFFFF;
  background-color: #b0c900;
}

.tab-hover {
  color: #FFFFFF;
  background-color: #D6E481;
  cursor: pointer;
  cursor: hand;
}

.no-info {
  text-align: center;
  color: #6495ED;
  margin-left: 1in;
  margin-right: 1in;
}

.title {
  text-align: center;
  font-size: 15pt;
  font-family: Verdana, sans-serif;
  font-weight: bold;
  padding-bottom: 0.4in;
}

.tab-definition {
  font-family: Verdana, sans-serif;
  line-height: 135%;
  color: #000094;
  margin: 0in 0.15in 0.75in 0.15in;
  padding: 0.5in 0.9in 0in 0.9in;
}

.separator {
  text-align: center;
  margin-bottom: 10pt;
}

img.plus-minus {
  border: 0px solid #fff;
}

/* achtergrondplaatjes */
div.achtergrond-nederland {
  background-image: url(<html:rewrite page="/js/metadataEditor/images/achtergrond-nederland.jpg"/>);
  background-repeat: no-repeat;
}

div.achtergrond-europa {
  background-image: url(<html:rewrite page="/js/metadataEditor/images/achtergrond-europa.jpg"/>);
  background-repeat: no-repeat;
}

div.achtergrond-wereld {
  background-image: url(<html:rewrite page="/js/metadataEditor/images/achtergrond-wereld.jpg"/>);
  background-repeat: no-repeat;
}

div.achtergrond-project {
  background-image: url(<html:rewrite page="/js/metadataEditor/images/achtergrond-project.jpg"/>);
  background-repeat: no-repeat;
}

div.achtergrond-webservice {
  background-image: url(<html:rewrite page="/js/metadataEditor/images/achtergrond-webservice.jpg"/>);
  background-repeat: no-repeat;
}