// Datatables
jQuery(document).ready(function() {
    (function($) {
        // Custom sorting
        jQuery.fn.dataTableExt.oSort['dutchdates-asc']  = function(a,b) { 
            var x = getDate($(a)), y = getDate($(b));
            return ((x < y) ? -1 : ((x > y) ?  1 : 0)); 
        }; 

        jQuery.fn.dataTableExt.oSort['dutchdates-desc'] = function(a,b) { 
            var x = getDate($(a)), y = getDate($(b));
            return ((x < y) ? 1 : ((x > y) ?  -1 : 0)); 
        };
        function getDate(obj) {
            var s = obj.text();
            var hit = s.match(/(\d{2})-(\d{2})-(\d{4})/);
            if (hit && hit.length === 4) {
                return new Date(hit[3], hit[2], hit[1]);
            }
            return new Date(s); 
        }
        $('.dataTable').each(function() {
            var dataTableObj = null, $table = $(this);
            // Setup sorting and filtering
            var columnSettings = [];
            var $filters = $('<tr class="filter"></tr>');
            var filterCount = 0;
            $table.find('thead tr').first().find('th').each(function(index) {
                var colSetting = {};
                var sortType = "string";
                if(this.className.match(/sorter\:[ ]?[']?digit[']?/)) {
                    sortType = "numeric";
                }
                if(this.className.match(/sorter\:[ ]?[']?dutchdates[']?/)) {
                    sortType = "dutchdates";
                }
                if(this.className.match(/sorter\:[ ]?[']?false[']?/)) {
                    colSetting.bSortable = false;
                }
                colSetting.sType = sortType;
                columnSettings.push(colSetting);
                if($(this).hasClass('no-filter')) {
                    $filters.append('<th>&nbsp;</th>');
                } else {
                    filterCount++;
                    var $filter = $('<input type="text" name="filter" title="' + $(this).text() + '" />').keyup(function(e) {
                        if(dataTableObj !== null) dataTableObj.fnFilter( this.value, index );
                    });
                    var $header = $('<th></th>').append($filter);
                    $filters.append($header);
                }
            });
            // Init datatable
            dataTableObj = $table.dataTable({
                "bSortClasses": false,
                "aoColumns": columnSettings,
                "bStateSave": true,
                "sPaginationType": "full_numbers",
                "oLanguage": {
                    "sLengthMenu": "_MENU_ items per pagina",
                    "sSearch": "Zoeken:",
                    "oPaginate": {
                        "sFirst": "Begin",
                        "sNext": "Volgende",
                        "sPrevious": "Vorige",
                        "sLast": "Einde",
                    },
                    "sInfo": "Items _START_ to _END_ van _TOTAL_ getoond",
                    "sInfoEmpty": "Geen items gevonden",
                    "sInfoFiltered": " - gefilterd (in totaal _MAX_ items)",
                    "sEmptyTable": "Geen items gevonden",
                    "sZeroRecords": "Geen items gevonden"
                }
            });
            // Append filters
            if(filterCount !== 0) $table.find('thead').append($filters);
            // Handle row click
            $table.find('tbody').delegate("td", "click", function(e) {
                var $row = $(this);
                if($row.find('a, input').length === 0) {
                    var link = $row.parent().attr('data-link');
                    if(link) window.location.href = link;
                } else {
                    console.log('contains link!');
                }
            });
            // Show table
            $table.css({
                'position': 'static',
                'left': '0px'
            });
        });
    })(jQuery);
});

// Legacy support
var Table = {
    stripe: function(obj, altrow) {
        jQuery(obj).find('tbody tr:odd').addClass(altrow);
    }
};