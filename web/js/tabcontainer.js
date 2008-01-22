
var tabCollections = new Object();
var tabCookieName = 'tabCookie';
function registerCollection(id, active)
{
    tabCollection = document.getElementById(id)
    if (tabCollection == null)
    {
        alert('TabCollection \'' + id + '\' could not be found!');
    } else {
        var tc = new TabContainer(tabCollection);
        tc.init();
        tabCollections[id] = tc;
        cookie = readCookie(id)
        if (cookie != null && cookie != '')
        {
            active = cookie;
        } 
        displayTabByReference(id, active);        
    }
}

function readCookie(cookieName) {
     var theCookie=""+document.cookie;
     var ind=theCookie.indexOf(cookieName);
     if (ind==-1 || cookieName=="") return ""; 
     var ind1=theCookie.indexOf(';',ind);
     if (ind1==-1) ind1=theCookie.length; 
     return unescape(theCookie.substring(ind+cookieName.length+1,ind1));
}


function displayTabBySource(tabSource){
    
    sourceParent = tabSource.parentNode.parentNode.parentNode;
    if (sourceParent.className == 'tabcollection'){
        displayTabByReference(sourceParent.id, tabSource.id);
    } else {
        alert('Could not locate collection for tabsource!');
    }
    
}

function displayTabByReference(collectionId, tabId)
{
    document.cookie = collectionId + '=' + tabId;
    tc = tabCollections[collectionId];    
    tc.activateTab(tabId);
}
 

function TabContainer(tce){           

    
    //Relational
    var sheets = new Array();
    var tabs = new Array();

    //Elemental
    var tabCollectionElement = tce;    

    this.init = function(){
        sheets = new Array();
        tabs = new Array();
        childList = tabCollectionElement.childNodes;
        for( var x = 0; x < childList.length; x++ ) {
            if (childList[x].id == 'tabs'){
               tabElements = childList[x].getElementsByTagName('li');
               for( var j = 0; j < tabElements.length; j++ ) {
                    tabElement = tabElements[j];
                    if (tabElement.id != null && tabElement.id != '')
                    {
                        tab = new Tab(this, tabElement);
                        tabs[tabElement.id] = tab;
                        tabs[tabs.length] = tab;
                    }
               }
            } else if (childList[x].id == 'sheets'){
                var sheetList = childList[x].childNodes;
                for( var y = 0; y < sheetList.length; y++ ) {
                    sheetElement = sheetList[y];
                    if (sheetElement.className == 'sheet')  {
                        var sheet =  new Sheet(this, sheetElement);
                        sheets[sheetElement.id] = sheet;   
                        sheets[sheets.length] = sheet;   
                        sheet.hide();
                    }
                }                
            }
        }
        //Match sheets with tabs!
        for( var y = 0; y < sheets.length; y++ ) {
            sheet = sheets[y];
            tab = tabs[sheet.getElement().id];
            if (tab == null)
            {
                alert('Tab with id \''+ sheet.getElement().id + '\' could not be found!');
            } else {
                tab.setSheet(sheet);
                sheet.setTab(tab);
            }
        }
    }

   this.activateTab = function(tabId){
        //Find the active tab!
        activeTab = tabs[tabId];
        if (activeTab == null){
            alert('Tab with id \'' + tabId + '\' could not be found!');
        } else {
            activeSheet = activeTab.getSheet();
            if (activeSheet != null)
            {
                //Hide all...
                for( var y = 0; y < sheets.length; y++ ) {
                    sheet = sheets[y];
                    sheet.hide();
                    if (sheet.getTab() != null)
                    {
                        sheet.getTab().setActive(false);
                    }
                }
                //Display active...
                activeTab.setActive(true);
                activeSheet.display();
            }
        }
   }
}


function Tab(tc, te){
    //Relational
    var tabContainer = tc;
    var sheet;
    
    //Elemental
    var tabElement = te;

    this.setActive = function(booActive){
        if (booActive == true)
        {
            tabElement.className = 'active'
        } else {
            tabElement.className = '' 
        }
    }

    this.setSheet = function(s){
        sheet = s;
    }
    this.getSheet = function()
    {
        return sheet;
    }
    this.getElement = function()
    {
        return tabElement;
    }

}

function Sheet(tc, se){
    //Relational
    var tabContainer = tc;
    var tab;
    
    //Elemental
    var sheetElement = se;

    this.display = function(){
        sheetElement.style.display ='block';
    }

    this.hide = function(){
        sheetElement.style.display ='none';
    }

    this.setTab = function(t)
    {
        tab = t;
    }
    this.getTab = function()
    {
        return tab;
    }

    this.getElement = function()
    {
        return sheetElement;
    }


}
