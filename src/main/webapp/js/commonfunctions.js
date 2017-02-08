// Datatables
jQuery(document).ready(function() {
    // Datatables
    jQuery('.dataTable').each(function() {
        new B3PDataTable(this);
    });
});

/**
 * B3P wrapper for the jQuery DataTables plugin
 * @requires jQuery
 * @requires jQuery.dataTables
 * @param {HTMLElement} table
 * @returns {B3PDataTable}
 */
function B3PDataTable(table) {
    
    /**
     * Setting to preserve table height even when there are less items
     * @type boolean
     */
    this.preserveHeight = true;
    
    /**
     * The jQuery object of the table
     * @type jQuery
     */
    this.table = jQuery(table);

    /**
     * Container for the creation of the DataTable object
     * @type DataTable
     */
    this.dataTableObj = null;

    /**
     * Container for the column settings
     * @type Array
     */
    this.columnSettings = [];

    /**
     * Header row to hold the filters
     * @type jQuery
     */
    this.filters = jQuery('<tr class="filter"></tr>');

    /**
     * The selected row object
     * @type jQuery
     */
    this.selectedRow = this.table.find('.row_selected');

    /**
     * Container for the DataTable settings
     * @type Object
     */
    this.tableSettings = null;
    
    /**
     * Initializes a B3PDataTable object to create a sortable, filterable DataTable
     * @returns {Boolean}
     */
    this.init = function() {
        var filterCount = this.initHeaders();
        if(this.preserveHeight) var tableHeight = this.calculatePageHeight(filterCount);
        var tableSettings = this.initDataTable();
        // If there is no saved state (first time view) go to selected row
        if(tableSettings.oLoadedState === null) {
            this.showSelectedRow();
        }
        this.appendSelectedRowButton();
        if(filterCount !== 0) {
            this.appendFilters();
        }
        this.initRowClick();
        if(this.preserveHeight) this.setWrapperHeight(tableHeight);
        this.showTable();
        return true;
    };
    
    /**
     * Calculate the height of the table header and first 10 rows (first page)
     * @param {int} number of filters
     * @returns {int}
     */
    this.calculatePageHeight = function(filterCount) {
        // Border margin of table
        var borderMargin = 2;
        // Calc header height
        var headerHeight = this.table.find('thead').outerHeight(true) + borderMargin;
        // If there are filters, multiply headerheight by 2
        if(filterCount !== 0) headerHeight = headerHeight * 2;
        // Calc content height
        var contentHeight = 0;
        // Use first 10 rows
        var count = 1;
        this.table.find('tbody').children().slice(0, 10).each(function() {
            contentHeight += jQuery(this).outerHeight(true) + borderMargin;
        });
        var tableHeight = headerHeight + contentHeight + 16; // margin;
        return tableHeight;
    };
    
    /**
     * Sets the minimum height of the wrapper, causing the table to preserve height
     * @param {int} tableHeight
     * @returns {Boolean}
     */
    this.setWrapperHeight = function(tableHeight) {
        var wrapper = this.table.parent();
        // Calculate height of search box and pagination
        var childHeight = 0;
        wrapper.find('.dataTables_filter, .dataTables_paginate').each(function() {
            childHeight += jQuery(this).outerHeight(true);
        });
        // Move info and pagination to the bottom
        jQuery('.dataTables_info, .dataTables_paginate').each(function() {
            var obj = jQuery(this);
            obj.css({
                'position': 'absolute',
                'bottom': '0px'
            });
            if(obj.hasClass('dataTables_paginate')) obj.css('right', '15px');
        });
        // Set wrapper min-height
        wrapper.css({
            'min-height': (childHeight + tableHeight - 19 + 'px') // Subtract 19 pixels for wrapper margin
        });
        return true;
    };
    
    /**
     * Initializes the headers: create column settings and append a filter.
     * Returns the amount of filters created.
     * @returns {int}
     */
    this.initHeaders = function() {
        var me = this, filterCount = 0;
        this.table.find('thead tr').first().find('th').each(function(index) {
            me.columnSettings.push(me.getColumnSettings(this));
            var hasFilter = me.createFilter(this, index);
            if(hasFilter) filterCount++;
        });
        return filterCount;
    };
    
    /**
     * Creates a DataTables column settings object based on some options
     * @param {type} the table header object
     * @returns {Object}
     */
    this.getColumnSettings = function(column) {
        var colSetting = {};
        var sortType = "string";
        if(column.className.match(/sorter\:[ ]?[']?digit[']?/)) {
            sortType = "numeric";
        }
        if(column.className.match(/sorter\:[ ]?[']?html[']?/)) {
            sortType = "html";
        }
        if(column.className.match(/sorter\:[ ]?[']?dutchdates[']?/)) {
            sortType = "dutchdates";
        }
        if(column.className.match(/sorter\:[ ]?[']?false[']?/)) {
            colSetting.bSortable = false;
        }
        colSetting.sType = sortType;
        return colSetting;
    };
    
    /**
     * Creates a filter box for a column. Returns true if a filter is created
     * @param {type} the table header object
     * @param {type} the column index
     * @returns {Boolean}
     */
    this.createFilter = function(column, index) {
        var me = this, col = jQuery(column);
        if(col.hasClass('no-filter')) {
            // Column has no-filter class, so create empty header
            this.filters.append('<th>&nbsp;</th>');
            return false;
        } else {
            var filter = jQuery('<input type="text" name="filter" title="' + col.text() + '" value="" />').keyup(function(e) {
                me.filterColumn(this.value, index);
            });
            var header = jQuery('<th></th>').append(filter);
            this.filters.append(header);
        }
        return true;
    };
    
    /**
     * Initializes the DataTables plugin and returns the creation settings
     * @returns {Object}
     */
    this.initDataTable = function() {
        this.dataTableObj = this.table.dataTable({
            "bSortClasses": false,
            "aoColumns": this.columnSettings,
            "bStateSave": true,
            "sPaginationType": "full_numbers",
            "oLanguage": {
                "sLengthMenu": "_MENU_ items per pagina",
                "sSearch": "Zoeken:",
                "oPaginate": {
                    "sFirst": "Begin",
                    "sNext": "Volgende",
                    "sPrevious": "Vorige",
                    "sLast": "Einde"
                },
                "sInfo": "Items _START_ tot _END_ van _TOTAL_ getoond",
                "sInfoEmpty": "Geen items gevonden",
                "sInfoFiltered": " - gefilterd (in totaal _MAX_ items)",
                "sEmptyTable": "Geen items gevonden",
                "sZeroRecords": "Geen items gevonden"
            }
        });
        this.tableSettings = this.dataTableObj.fnSettings();
        return this.tableSettings;
    };
    
    /**
     * Append the button to go to the selected row to the paginiation
     * @returns {Boolean}
     */
    this.appendSelectedRowButton = function() {
        var me = this;
        me.table.parent().find('.dataTables_paginate').append(jQuery('<a>Geselecteerd</a>').addClass('paginate_button').click(function(e){
            e.preventDefault();
            me.showSelectedRow();
        }));
        return true;
    };
    
    /**
     * Append the filter boxes to the table header
     * @returns {Boolean}
     */
    this.appendFilters = function() {
        var me = this;
        // Get saved searches to repopulate field with search value
        me.filters.find('th').each(function(index) {
            var searchCol = me.tableSettings.aoPreSearchCols[index];
            if(searchCol.hasOwnProperty('sSearch') && searchCol.sSearch.length !== 0) {
                jQuery('input', this).val(searchCol.sSearch);
            }
        });
        // Add filters to table
        me.table.find('thead').append(me.filters);
        return true;
    };
    
    /**
     * Attach handlers for clicking a row in the table
     * @returns {Boolean}
     */
    this.initRowClick = function() {
        this.table.find('tbody').delegate("td", "click", function() {
            var row = jQuery(this);
            // Check if there is a link or input (button, checkbox, etc.) present
            if(row.find('a, input').length === 0) {
                // No link or input so navigate to the attached URL
                var link = row.parent().attr('data-link');
                if(link) window.location.href = link;
            }
        });
        return true;
    };
    
    /**
     * Search a single column for a string
     * @param {String} the string to search on
     * @param {int} the index of the columns that needs to be filtered
     * @returns {Boolean}
     */
    this.filterColumn = function(filter, colindex) {
        if(this.dataTableObj === null) return;
        this.dataTableObj.fnFilter( filter, colindex );
        return true;
    };
    
    /**
     * Shows the currently selected row
     * @returns {Boolean}
     */
    this.showSelectedRow = function() {
        if(this.dataTableObj === null) return false;
        this.dataTableObj.fnDisplayRow( this.selectedRow[0] );
        return true;
    };
    
    /**
     * Makes the table visible (table is hidden using CSS and positioning to prevent flicker)
     * @returns {Boolean}
     */
    this.showTable = function() {
        this.table.css({
            'position': 'static',
            'left': '0px'
        });
        return true;
    };  

    /**
     * Install dataTable extensions, needed for some functionality
     */
    (function() {
        if(typeof jQuery.fn.dataTableExt.oSort['dutchdates-asc'] === "undefined") {
            /**
             * Extension to support sorting of dutch formatted dates (dd-mm-yyyy)
             */
            jQuery.fn.dataTableExt.oSort['dutchdates-asc']  = function(a,b) { 
                var x = parseDutchDate(jQuery(a)), y = parseDutchDate(jQuery(b));
                return ((x < y) ? -1 : ((x > y) ?  1 : 0)); 
            }; 
            jQuery.fn.dataTableExt.oSort['dutchdates-desc'] = function(a,b) { 
                var x = parseDutchDate(jQuery(a)), y = parseDutchDate(jQuery(b));
                return ((x < y) ? 1 : ((x > y) ?  -1 : 0)); 
            };
            var parseDutchDate = function(obj) {

                var s = obj.selector;
                var hit = s.match(/(\d{2})-(\d{2})-(\d{4})/);
                if (hit && hit.length === 4) return new Date(hit[3], hit[2], hit[1]);
                return new Date(s); 
            };
        }
        if(typeof jQuery.fn.dataTableExt.oApi.fnDisplayRow === "undefined") {
            /**
             * Extension to go to the page containing a specific row
             * We use this extension to go to the selected row
             */
            jQuery.fn.dataTableExt.oApi.fnDisplayRow = function ( oSettings, nRow ) {
                // Account for the "display" all case - row is already displayed
                if ( oSettings._iDisplayLength === -1 ) return;
                // Find the node in the table
                var iPos = -1, iLen=oSettings.aiDisplay.length;
                for( var i=0; i < iLen; i++ ) {
                    if( oSettings.aoData[ oSettings.aiDisplay[i] ].nTr === nRow ) {
                        iPos = i;
                        break;
                    }
                }
                // Alter the start point of the paging display
                if( iPos >= 0 ) {
                    oSettings._iDisplayStart = ( Math.floor(i / oSettings._iDisplayLength) ) * oSettings._iDisplayLength;
                    this.oApi._fnCalculateEnd( oSettings );
                }
                this.oApi._fnDraw( oSettings );
            };
        }
    })();
    
    /**
     * Execute initialize function
     */
    this.init();
}

// Legacy support
var Table = {
    stripe: function(obj, altrow) {
        jQuery(obj).find('tbody tr:odd').addClass(altrow);
    }
};