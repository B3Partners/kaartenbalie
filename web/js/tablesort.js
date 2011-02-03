/**
 * Cookie plugin
 *
 * Copyright (c) 2006 Klaus Hartl (stilbuero.de)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */
eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('r.5=w(k,d,a){4(m d!=\'H\'){a=a||{};4(d===p){d=\'\';a.3=-1}2 g=\'\';4(a.3&&(m a.3==\'n\'||a.3.u)){2 f;4(m a.3==\'n\'){f=G E();f.C(f.B()+(a.3*z*s*s*v))}o{f=a.3}g=\'; 3=\'+f.u()}2 b=a.7?\'; 7=\'+(a.7):\'\';2 e=a.9?\'; 9=\'+(a.9):\'\';2 l=a.t?\'; t\':\'\';6.5=[k,\'=\',K(d),g,b,e,l].I(\'\')}o{2 h=p;4(6.5&&6.5!=\'\'){2 c=6.5.F(\';\');D(2 i=0;i<c.8;i++){2 j=r.A(c[i]);4(j.q(0,k.8+1)==(k+\'=\')){h=y(j.q(k.8+1));x}}}J h}};',47,47,'||var|expires|if|cookie|document|path|length|domain|||||||||||||typeof|number|else|null|substring|jQuery|60|secure|toUTCString|1000|function|break|decodeURIComponent|24|trim|getTime|setTime|for|Date|split|new|undefined|join|return|encodeURIComponent'.split('|'),0,{}));

/* Copyright (c) 2007 Paul Bakaus (paul.bakaus@googlemail.com) and Brandon Aaron (brandon.aaron@gmail.com || http://brandonaaron.net)
 * Dual licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
 * and GPL (http://www.opensource.org/licenses/gpl-license.php) licenses.
 *
 * $LastChangedDate: 2007-12-20 08:43:48 -0600 (Thu, 20 Dec 2007) $
 * $Rev: 4257 $
 *
 * Version: 1.2
 *
 * Requires: jQuery 1.2+
 */
(function($){$.dimensions={version:'1.2'};$.each(['Height','Width'],function(i,name){$.fn['inner'+name]=function(){if(!this[0])return;var torl=name=='Height'?'Top':'Left',borr=name=='Height'?'Bottom':'Right';return this.is(':visible')?this[0]['client'+name]:num(this,name.toLowerCase())+num(this,'padding'+torl)+num(this,'padding'+borr);};$.fn['outer'+name]=function(options){if(!this[0])return;var torl=name=='Height'?'Top':'Left',borr=name=='Height'?'Bottom':'Right';options=$.extend({margin:false},options||{});var val=this.is(':visible')?this[0]['offset'+name]:num(this,name.toLowerCase())+num(this,'border'+torl+'Width')+num(this,'border'+borr+'Width')+num(this,'padding'+torl)+num(this,'padding'+borr);return val+(options.margin?(num(this,'margin'+torl)+num(this,'margin'+borr)):0);};});$.each(['Left','Top'],function(i,name){$.fn['scroll'+name]=function(val){if(!this[0])return;return val!=undefined?this.each(function(){this==window||this==document?window.scrollTo(name=='Left'?val:$(window)['scrollLeft'](),name=='Top'?val:$(window)['scrollTop']()):this['scroll'+name]=val;}):this[0]==window||this[0]==document?self[(name=='Left'?'pageXOffset':'pageYOffset')]||$.boxModel&&document.documentElement['scroll'+name]||document.body['scroll'+name]:this[0]['scroll'+name];};});$.fn.extend({position:function(){var left=0,top=0,elem=this[0],offset,parentOffset,offsetParent,results;if(elem){offsetParent=this.offsetParent();offset=this.offset();parentOffset=offsetParent.offset();offset.top-=num(elem,'marginTop');offset.left-=num(elem,'marginLeft');parentOffset.top+=num(offsetParent,'borderTopWidth');parentOffset.left+=num(offsetParent,'borderLeftWidth');results={top:offset.top-parentOffset.top,left:offset.left-parentOffset.left};}return results;},offsetParent:function(){var offsetParent=this[0].offsetParent;while(offsetParent&&(!/^body|html$/i.test(offsetParent.tagName)&&$.css(offsetParent,'position')=='static'))offsetParent=offsetParent.offsetParent;return $(offsetParent);}});function num(el,prop){return parseInt($.curCSS(el.jquery?el[0]:el,prop,true))||0;};})(jQuery);

/*
 *
 * TableSorter 2.0 - Client-side table sorting with ease!
 * Version 2.0.3
 * @requires jQuery v1.2.3
 *
 * Copyright (c) 2007 Christian Bach
 * Examples and docs at: http://tablesorter.com
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */
/**
 *
 * @description Create a sortable table with multi-column sorting capabilitys
 *
 * @example $('table').tablesorter();
 * @desc Create a simple tablesorter interface.
 *
 * @example $('table').tablesorter({ sortList:[[0,0],[1,0]] });
 * @desc Create a tablesorter interface and sort on the first and secound column in ascending order.
 *
 * @example $('table').tablesorter({ headers: { 0: { sorter: false}, 1: {sorter: false} } });
 * @desc Create a tablesorter interface and disableing the first and secound column headers.
 *
 * @example $('table').tablesorter({ 0: {sorter:"integer"}, 1: {sorter:"currency"} });
 * @desc Create a tablesorter interface and set a column parser for the first and secound column.
 *
 *
 * @param Object settings An object literal containing key/value pairs to provide optional settings.
 *
 * @option String cssHeader (optional) 			A string of the class name to be appended to sortable tr elements in the thead of the table.
 * 												Default value: "header"
 *
 * @option String cssAsc (optional) 			A string of the class name to be appended to sortable tr elements in the thead on a ascending sort.
 * 												Default value: "headerSortUp"
 *
 * @option String cssDesc (optional) 			A string of the class name to be appended to sortable tr elements in the thead on a descending sort.
 * 												Default value: "headerSortDown"
 *
 * @option String sortInitialOrder (optional) 	A string of the inital sorting order can be asc or desc.
 * 												Default value: "asc"
 *
 * @option String sortMultisortKey (optional) 	A string of the multi-column sort key.
 * 												Default value: "shiftKey"
 *
 * @option String textExtraction (optional) 	A string of the text-extraction method to use.
 * 												For complex html structures inside td cell set this option to "complex",
 * 												on large tables the complex option can be slow.
 * 												Default value: "simple"
 *
 * @option Object headers (optional) 			An array containing the forces sorting rules.
 * 												This option let's you specify a default sorting rule.
 * 												Default value: null
 *
 * @option Array sortList (optional) 			An array containing the forces sorting rules.
 * 												This option let's you specify a default sorting rule.
 * 												Default value: null
 *
 * @option Array sortForce (optional) 			An array containing forced sorting rules.
 * 												This option let's you specify a default sorting rule, which is prepended to user-selected rules.
 * 												Default value: null
 *
  * @option Array sortAppend (optional) 			An array containing forced sorting rules.
 * 												This option let's you specify a default sorting rule, which is appended to user-selected rules.
 * 												Default value: null
 *
 * @option Boolean widthFixed (optional) 		Boolean flag indicating if tablesorter should apply fixed widths to the table columns.
 * 												This is usefull when using the pager companion plugin.
 * 												This options requires the dimension jquery plugin.
 * 												Default value: false
 *
 * @option Boolean cancelSelection (optional) 	Boolean flag indicating if tablesorter should cancel selection of the table headers text.
 * 												Default value: true
 *
 * @option Boolean debug (optional) 			Boolean flag indicating if tablesorter should display debuging information usefull for development.
 *
 * @type jQuery
 *
 * @name tablesorter
 *
 * @cat Plugins/Tablesorter
 *
 * @author Christian Bach/christian.bach@polyester.se
 */

(function($) {
	$.extend({
		tablesorter: new function() {

			var parsers = [], widgets = [];

			this.defaults = {
				cssHeader: "header",
				cssAsc: "headerSortUp",
				cssDesc: "headerSortDown",
				sortInitialOrder: "asc",
				sortMultiSortKey: "shiftKey",
				sortForce: null,
				sortAppend: null,
				textExtraction: "simple",
				parsers: {},
				widgets: [],
				widgetZebra: {css: ["even","odd"]},
				headers: {},
				widthFixed: false,
				cancelSelection: true,
				sortList: [],
				headerList: [],
				dateFormat: "us",
				decimal: '.',
				debug: false
			};

			/* debuging utils */
			function benchmark(s,d) {
				log(s + ": " + (new Date().getTime() - d.getTime()) + "ms");
			}

			this.benchmark = benchmark;

			function log(s) {
				if (console !== undefined && console.debug !== undefined) {
					console.log(s);
				} else {
					alert(s);
				}
			}

			/* parsers utils */
			function buildParserCache(table,$headers) {

				if(table.config.debug) { var parsersDebug = ""; }

				var rows = table.tBodies[0].rows;

				if(table.tBodies[0].rows[0]) {

					var list = [], cells = rows[0].cells, l = cells.length;

					for (var i=0;i < l; i++) {
						var p = false;

						if($.metadata && ($($headers[i]).metadata() && $($headers[i]).metadata().sorter)  ) {

							p = getParserById($($headers[i]).metadata().sorter);

						} else if((table.config.headers[i] && table.config.headers[i].sorter)) {

							p = getParserById(table.config.headers[i].sorter);
						}
						if(!p) {
							p = detectParserForColumn(table,cells[i]);
						}

						if(table.config.debug) { parsersDebug += "column:" + i + " parser:" +p.id + "\n"; }

						list.push(p);
					}
				}

				if(table.config.debug) { log(parsersDebug); }

				return list;
			};

			function detectParserForColumn(table,node) {
				var l = parsers.length;
				for(var i=1; i < l; i++) {
					if(parsers[i].is($.trim(getElementText(table.config,node)),table,node)) {
						return parsers[i];
					}
				}
				// 0 is always the generic parser (text)
				return parsers[0];
			}

			function getParserById(name) {
				var l = parsers.length;
				for(var i=0; i < l; i++) {
					if(parsers[i].id.toLowerCase() == name.toLowerCase()) {
						return parsers[i];
					}
				}
				return false;
			}

			/* utils */
			function buildCache(table) {

				if(table.config.debug) { var cacheTime = new Date(); }


				var totalRows = (table.tBodies[0] && table.tBodies[0].rows.length) || 0,
					totalCells = (table.tBodies[0].rows[0] && table.tBodies[0].rows[0].cells.length) || 0,
					parsers = table.config.parsers,
					cache = {row: [], normalized: []};

					for (var i=0;i < totalRows; ++i) {

						/** Add the table data to main data array */
						var c = table.tBodies[0].rows[i], cols = [];

						cache.row.push($(c));

						for(var j=0; j < totalCells; ++j) {
							cols.push(parsers[j].format(getElementText(table.config,c.cells[j]),table,c.cells[j]));
						}

						cols.push(i); // add position for rowCache
						cache.normalized.push(cols);
						cols = null;
					};

				if(table.config.debug) { benchmark("Built cache for " + totalRows + " rows", cacheTime); }

				return cache;
			};

			function getElementText(config,node) {

				if(!node) return "";

				var t = "";

				if(config.textExtraction == "simple") {
					if(node.childNodes[0] && node.childNodes[0].hasChildNodes()) {
						t = node.childNodes[0].innerHTML;
					} else {
						t = node.innerHTML;
					}
				} else {
					if(typeof(config.textExtraction) == "function") {
						t = config.textExtraction(node);
					} else {
						t = $(node).text();
					}
				}
				return t;
			}

			function appendToTable(table,cache) {

				if(table.config.debug) {var appendTime = new Date()}

				var c = cache,
					r = c.row,
					n= c.normalized,
					totalRows = n.length,
					tableBody = $(table.tBodies[0]),
					rows = [];

				if (totalRows > 0) {
					var checkCell = (n[0].length-1);

					for (var i=0;i < totalRows; i++) {
						rows.push(r[n[i][checkCell]]);
						if(!table.config.appender) {

							var o = r[n[i][checkCell]];
							var l = o.length;
							for(var j=0; j < l; j++) {

								tableBody[0].appendChild(o[j]);

							}

							//tableBody.append(r[n[i][checkCell]]);
						}
					}
				}

				if(table.config.appender) {

					table.config.appender(table,rows);
				}

				rows = null;

				if(table.config.debug) { benchmark("Rebuilt table", appendTime); }

				//apply table widgets
				applyWidget(table);

				// trigger sortend
				setTimeout(function() {
					$(table).trigger("sortEnd");
				},0);

			};

			function buildHeaders(table) {

				if(table.config.debug) { var time = new Date(); }

				var meta = ($.metadata) ? true : false, tableHeadersRows = [];

				for(var i = 0; i < table.tHead.rows.length; i++) { tableHeadersRows[i]=0; };

				$tableHeaders = $("thead th",table);

				$tableHeaders.each(function(index) {

					this.count = 0;
					this.column = index;
					this.order = formatSortingOrder(table.config.sortInitialOrder);

					if(checkHeaderMetadata(this) || checkHeaderOptions(table,index)) this.sortDisabled = true;

					if(!this.sortDisabled) {
						$(this).addClass(table.config.cssHeader);
					}

					// add cell to headerList
					table.config.headerList[index]= this;
				});

				if(table.config.debug) { benchmark("Built headers", time); log($tableHeaders); }

				return $tableHeaders;

			};

		   	function checkCellColSpan(table, rows, row) {
                var arr = [], r = table.tHead.rows, c = r[row].cells;

				for(var i=0; i < c.length; i++) {
					var cell = c[i];

					if ( cell.colSpan > 1) {
						arr = arr.concat(checkCellColSpan(table, headerArr,row++));
					} else  {
						if(table.tHead.length == 1 || (cell.rowSpan > 1 || !r[row+1])) {
							arr.push(cell);
						}
						//headerArr[row] = (i+row);
					}
				}
				return arr;
			};

			function checkHeaderMetadata(cell) {
				if(($.metadata) && ($(cell).metadata().sorter === false)) { return true; };
				return false;
			}

			function checkHeaderOptions(table,i) {
				if((table.config.headers[i]) && (table.config.headers[i].sorter === false)) { return true; };
				return false;
			}

			function applyWidget(table) {
				var c = table.config.widgets;
				var l = c.length;
				for(var i=0; i < l; i++) {

					getWidgetById(c[i]).format(table);
				}

			}

			function getWidgetById(name) {
				var l = widgets.length;
				for(var i=0; i < l; i++) {
					if(widgets[i].id.toLowerCase() == name.toLowerCase() ) {
						return widgets[i];
					}
				}
			};

			function formatSortingOrder(v) {

				if(typeof(v) != "Number") {
					i = (v.toLowerCase() == "desc") ? 1 : 0;
				} else {
					i = (v == (0 || 1)) ? v : 0;
				}
				return i;
			}

			function isValueInArray(v, a) {
				var l = a.length;
				for(var i=0; i < l; i++) {
					if(a[i][0] == v) {
						return true;
					}
				}
				return false;
			}

			function setHeadersCss(table,$headers, list, css) {
				// remove all header information
				$headers.removeClass(css[0]).removeClass(css[1]);

				var h = [];
				$headers.each(function(offset) {
						if(!this.sortDisabled) {
							h[this.column] = $(this);
						}
				});

				var l = list.length;
				for(var i=0; i < l; i++) {
					h[list[i][0]].addClass(css[list[i][1]]);
				}
			}

			function fixColumnWidth(table,$headers) {
				var c = table.config;
				if(c.widthFixed) {
					var colgroup = $('<colgroup>');
					$("tr:first td",table.tBodies[0]).each(function() {
						colgroup.append($('<col>').css('width',$(this).width()));
					});
					$(table).prepend(colgroup);
				};
			}

			function updateHeaderSortCount(table,sortList) {
				var c = table.config, l = sortList.length;
				for(var i=0; i < l; i++) {
					var s = sortList[i], o = c.headerList[s[0]];
					o.count = s[1];
					o.count++;
				}
			}

			/* sorting methods */
			function multisort(table,sortList,cache) {

				if(table.config.debug) { var sortTime = new Date(); }

				var dynamicExp = "var sortWrapper = function(a,b) {", l = sortList.length;

				for(var i=0; i < l; i++) {

					var c = sortList[i][0];
					var order = sortList[i][1];
					var s = (getCachedSortType(table.config.parsers,c) == "text") ? ((order == 0) ? "sortText" : "sortTextDesc") : ((order == 0) ? "sortNumeric" : "sortNumericDesc");

					var e = "e" + i;

					dynamicExp += "var " + e + " = " + s + "(a[" + c + "],b[" + c + "]); ";
					dynamicExp += "if(" + e + ") { return " + e + "; } ";
					dynamicExp += "else { ";
				}

				// if value is the same keep orignal order
				var orgOrderCol = cache.normalized[0].length - 1;
				dynamicExp += "return a[" + orgOrderCol + "]-b[" + orgOrderCol + "];";

				for(var i=0; i < l; i++) {
					dynamicExp += "}; ";
				}

				dynamicExp += "return 0; ";
				dynamicExp += "}; ";

				eval(dynamicExp);

				cache.normalized.sort(sortWrapper);

				if(table.config.debug) { benchmark("Sorted " + sortList.toString(), sortTime); }

				return cache;
			};

			function sortText(a,b) {
				return ((a < b) ? -1 : ((a > b) ? 1 : 0));
			};

			function sortTextDesc(a,b) {
				return ((b < a) ? -1 : ((b > a) ? 1 : 0));
			};

	 		function sortNumeric(a,b) {
				return a-b;
			};

			function sortNumericDesc(a,b) {
				return b-a;
			};

			function getCachedSortType(parsers,i) {
				return parsers[i].type;
			};

			/* public methods */
			this.construct = function(settings) {

				return this.each(function() {

					if(!this.tHead || !this.tBodies) return;

					var $this, $document,$headers, cache, config, shiftDown = 0, sortOrder;

					this.config = {};

					config = $.extend(this.config, $.tablesorter.defaults, settings);

					// store common expression for speed
					$this = $(this);

					// build headers
					$headers = buildHeaders(this);

					// try to auto detect column type, and store in tables config
					this.config.parsers = buildParserCache(this,$headers);


					// build the cache for the tbody cells
					cache = buildCache(this);

					// store a copy of the original cache of all rows
					this.config.cache = cache;

					// get the css class names, could be done else where.
					var sortCSS = [config.cssDesc,config.cssAsc];

					// fixate columns if the users supplies the fixedWidth option
					fixColumnWidth(this);

					// apply event handling to headers
					// this is to big, perhaps break it out?
					$headers.click(function(e) {

						$this.trigger("sortStart");

						var totalRows = ($this[0].tBodies[0] && $this[0].tBodies[0].rows.length) || 0;

						if(!this.sortDisabled && totalRows > 0) {


							// store exp, for speed
							var $cell = $(this);

							// get current column index
							var i = this.column;

							// get current column sort order
							this.order = this.count++ % 2;

							// user only wants to sort on one column
							if(!e[config.sortMultiSortKey]) {

								// flush the sort list
								config.sortList = [];

								if(config.sortForce != null) {
									var a = config.sortForce;
									for(var j=0; j < a.length; j++) {
										if(a[j][0] != i) {
											config.sortList.push(a[j]);
										}
									}
								}

								// add column to sort list
								config.sortList.push([i,this.order]);

							// multi column sorting
							} else {
								// the user has clicked on an all ready sortet column.
								if(isValueInArray(i,config.sortList)) {

									// revers the sorting direction for all tables.
									for(var j=0; j < config.sortList.length; j++) {
										var s = config.sortList[j], o = config.headerList[s[0]];
										if(s[0] == i) {
											o.count = s[1];
											o.count++;
											s[1] = o.count % 2;
										}
									}
								} else {
									// add column to sort list array
									config.sortList.push([i,this.order]);
								}
							};
							setTimeout(function() {
								//set css for headers
								setHeadersCss($this[0],$headers,config.sortList,sortCSS);
								appendToTable($this[0],multisort($this[0],config.sortList,cache));
							},1);
							// stop normal event by returning false
							return false;
						}
					// cancel selection
					}).mousedown(function() {
						if(config.cancelSelection) {
							this.onselectstart = function() {return false};
							return false;
						}
					});

					// apply easy methods that trigger binded events
					$this.bind("update",function() {

						// rebuild parsers.
						this.config.parsers = buildParserCache(this,$headers);

						// rebuild the cache map
						cache = buildCache(this);

					}).bind("sorton",function(e,list) {

						$(this).trigger("sortStart");

						config.sortList = list;

						// update and store the sortlist
						var sortList = config.sortList;

						// update header count index
						updateHeaderSortCount(this,sortList);

						//set css for headers
						setHeadersCss(this,$headers,sortList,sortCSS);


						// sort the table and append it to the dom
						appendToTable(this,multisort(this,sortList,cache));

					}).bind("appendCache",function() {

						appendToTable(this,cache);

					}).bind("applyWidgetId",function(e,id) {

						getWidgetById(id).format(this);

					}).bind("applyWidgets",function() {
						// apply widgets
						applyWidget(this);
					});

					if($.metadata && ($(this).metadata() && $(this).metadata().sortlist)) {
						config.sortList = $(this).metadata().sortlist;
					}
					// if user has supplied a sort list to constructor.
					if(config.sortList.length > 0) {
						$this.trigger("sorton",[config.sortList]);
					}

					// apply widgets
					applyWidget(this);
				});
			};

			this.addParser = function(parser) {
				var l = parsers.length, a = true;
				for(var i=0; i < l; i++) {
					if(parsers[i].id.toLowerCase() == parser.id.toLowerCase()) {
						a = false;
					}
				}
				if(a) { parsers.push(parser); };
			};

			this.addWidget = function(widget) {
				widgets.push(widget);
			};

			this.formatFloat = function(s) {
				var i = parseFloat(s);
				return (isNaN(i)) ? 0 : i;
			};
			this.formatInt = function(s) {
				var i = parseInt(s);
				return (isNaN(i)) ? 0 : i;
			};

			this.isDigit = function(s,config) {
				var DECIMAL = '\\' + config.decimal;
				var exp = '/(^[+]?0(' + DECIMAL +'0+)?$)|(^([-+]?[1-9][0-9]*)$)|(^([-+]?((0?|[1-9][0-9]*)' + DECIMAL +'(0*[1-9][0-9]*)))$)|(^[-+]?[1-9]+[0-9]*' + DECIMAL +'0+$)/';
				return RegExp(exp).test($.trim(s));
			};

			this.clearTableBody = function(table) {
				if($.browser.msie) {
					function empty() {
						while ( this.firstChild ) this.removeChild( this.firstChild );
					}
					empty.apply(table.tBodies[0]);
				} else {
					table.tBodies[0].innerHTML = "";
				}
			};
		}
	});

	// extend plugin scope
	$.fn.extend({
        tablesorter: $.tablesorter.construct
	});

	var ts = $.tablesorter;

	// add default parsers
	ts.addParser({
		id: "text",
		is: function(s) {
			return true;
		},
		format: function(s) {
			return $.trim(s.toLowerCase());
		},
		type: "text"
	});

	ts.addParser({
		id: "digit",
		is: function(s,table) {
			var c = table.config;
			return $.tablesorter.isDigit(s,c);
		},
		format: function(s) {
			return $.tablesorter.formatFloat(s);
		},
		type: "numeric"
	});

	ts.addParser({
		id: "currency",
		is: function(s) {
			return /^[£$€?.]/.test(s);
		},
		format: function(s) {
			return $.tablesorter.formatFloat(s.replace(new RegExp(/[^0-9.]/g),""));
		},
		type: "numeric"
	});

	ts.addParser({
		id: "ipAddress",
		is: function(s) {
			return /^\d{2,3}[\.]\d{2,3}[\.]\d{2,3}[\.]\d{2,3}$/.test(s);
		},
		format: function(s) {
			var a = s.split("."), r = "", l = a.length;
			for(var i = 0; i < l; i++) {
				var item = a[i];
			   	if(item.length == 2) {
					r += "0" + item;
			   	} else {
					r += item;
			   	}
			}
			return $.tablesorter.formatFloat(r);
		},
		type: "numeric"
	});

	ts.addParser({
		id: "url",
		is: function(s) {
			return /^(https?|ftp|file):\/\/$/.test(s);
		},
		format: function(s) {
			return jQuery.trim(s.replace(new RegExp(/(https?|ftp|file):\/\//),''));
		},
		type: "text"
	});

	ts.addParser({
		id: "isoDate",
		is: function(s) {
			return /^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/.test(s);
		},
		format: function(s) {
			return $.tablesorter.formatFloat((s != "") ? new Date(s.replace(new RegExp(/-/g),"/")).getTime() : "0");
		},
		type: "numeric"
	});

	ts.addParser({
		id: "percent",
		is: function(s) {
			return /\%$/.test($.trim(s));
		},
		format: function(s) {
			return $.tablesorter.formatFloat(s.replace(new RegExp(/%/g),""));
		},
		type: "numeric"
	});

	ts.addParser({
		id: "usLongDate",
		is: function(s) {
			return s.match(new RegExp(/^[A-Za-z]{3,10}\.? [0-9]{1,2}, ([0-9]{4}|'?[0-9]{2}) (([0-2]?[0-9]:[0-5][0-9])|([0-1]?[0-9]:[0-5][0-9]\s(AM|PM)))$/));
		},
		format: function(s) {
			return $.tablesorter.formatFloat(new Date(s).getTime());
		},
		type: "numeric"
	});

	ts.addParser({
		id: "shortDate",
		is: function(s) {
			return /\d{1,2}[\/\-]\d{1,2}[\/\-]\d{2,4}/.test(s);
		},
		format: function(s,table) {
			var c = table.config;
			s = s.replace(/\-/g,"/");
			if(c.dateFormat == "us") {
				// reformat the string in ISO format
				s = s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})/, "$3/$1/$2");
			} else if(c.dateFormat == "uk") {
				//reformat the string in ISO format
				s = s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})/, "$3/$2/$1");
			} else if(c.dateFormat == "dd/mm/yy" || c.dateFormat == "dd-mm-yy") {
				s = s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{2})/, "$1/$2/$3");
			}
			return $.tablesorter.formatFloat(new Date(s).getTime());
		},
		type: "numeric"
	});

	ts.addParser({
	    id: "time",
	    is: function(s) {
	        return /^(([0-2]?[0-9]:[0-5][0-9])|([0-1]?[0-9]:[0-5][0-9]\s(am|pm)))$/.test(s);
	    },
	    format: function(s) {
	        return $.tablesorter.formatFloat(new Date("2000/01/01 " + s).getTime());
	    },
	  type: "numeric"
	});


	ts.addParser({
	    id: "metadata",
	    is: function(s) {
	        return false;
	    },
	    format: function(s,table,cell) {
			var c = table.config, p = (!c.parserMetadataName) ? 'sortValue' : c.parserMetadataName;
	        return $(cell).metadata()[p];
	    },
	  type: "numeric"
	});

	// add default widgets
	ts.addWidget({
		id: "zebra",
		format: function(table) {
			if(table.config.debug) { var time = new Date(); }
			$("tr:visible",table.tBodies[0])
	        .filter(':even')
	        .removeClass(table.config.widgetZebra.css[1]).addClass(table.config.widgetZebra.css[0])
	        .end().filter(':odd')
	        .removeClass(table.config.widgetZebra.css[0]).addClass(table.config.widgetZebra.css[1]);
			if(table.config.debug) { $.tablesorter.benchmark("Applied Zebra widget", time); }
		}
	});
})(jQuery);

/*
 * Copyright (c) 2008 Justin Britten justinbritten at gmail.com
 *
 * Some code was borrowed from:
 * 1. Greg Weber's uiTableFilter project (http://gregweber.info/projects/uitablefilter)
 * 2. Denny Ferrassoli & Charles Christolini's TypeWatch project (www.dennydotnet.com)
 *
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */


(function($) {
  $.extend({
    tablesorterFilter: new function() {

      // Default filterFunction implementation (element text, search words, case-sensitive flag)
      function has_words(str, words, caseSensitive) {
        var text = caseSensitive ? str : str.toLowerCase();

        for (var i=0; i < words.length; i++) {
          if (words[i].charAt(0) == '-') {
            if (text.indexOf(words[i].substr(1)) != -1) return false; // Negated word must not be in text
          } else if (text.indexOf(words[i]) == -1) return false; // Normal word must be in text
        }

        return true;
      }


      function doFilter(table) {
        if(table.config.debug) { var cacheTime = new Date(); }

        // Build multiple filters from input boxes
        // TODO: enable incremental filtering by caching result and applying only single filter action
        var filters = [];
        for(var i=0; i < table.config.filter.length; i++) {
          var container = $(table.config.filter[i].filterContainer);
          // Trim and unify whitespace before splitting
          var phrase = jQuery.trim(container.val()).replace(/\s+/g, ' ');
          if(phrase.length != 0) {
            var caseSensitive = table.config.filter[i].filterCaseSensitive;
            filters.push({
              caseSensitive: caseSensitive,
              words: caseSensitive ? phrase.split(" ") : phrase.toLowerCase().split(" "),
              findStr: table.config.filter[i].filterColumns ? "td:eq(" + table.config.filter[i].filterColumns.join("),td:eq(") + ")" : "",
              filterFunction: table.config.filter[i].filterFunction
            });
          }
        }
        var filterCount = filters.length;

        // Filter cleared?
        if(filterCount == 0) {
          var search_text = function() {
            var elem = jQuery(this);
            resultRows[resultRows.length] = elem;
          }
        } else {
          var search_text = function() {
            var elem = jQuery(this);
            for(var i=0; i < filterCount; i++) {
              if(! filters[i].filterFunction( (filters[i].findStr ? elem.find(filters[i].findStr) : elem).text(), filters[i].words, filters[i].caseSensitive)) {
                return true; // Skip elem and continue to next element
              }
            }
            resultRows[resultRows.length] = elem;
          }
        }

        // Walk through all of the table's rows and search.
        // Rows which match the string will be pushed into the resultRows array.
        var allRows = table.config.cache.row;
        var resultRows = [];

        if(filterCount == 0) {
            resultRows = allRows;
        } else {
            var allRowsCount = allRows.length;
            for (var i=0; i < allRowsCount; i++) {
              allRows[i].each ( search_text );
            }
        }

        // Clear the table
        $.tablesorter.clearTableBody(table);

        // Push all rows which matched the search string onto the table for display.
        var resultRowsCount = resultRows.length;
        for (var i=0; i < resultRowsCount; i++) {
          $(table.tBodies[0]).append(resultRows[i]);
        }

        // Update the table by executing some of tablesorter's triggers
        // This will apply any widgets or pagination, if used.
        $(table).trigger("update");
        if (resultRows.length) {
          $(table).trigger("appendCache");
          // Apply current sorting after restoring rows
          $(table).trigger("sorton", [table.config.sortList]);
        }

        if(table.config.debug) { $.tablesorter.benchmark("Apply filter:", cacheTime); }

        // Inform subscribers that filtering finished
        $(table).trigger("filterEnd");

        return table;
      };

      function clearFilter(table) {
        if(table.config.debug) { var cacheTime = new Date(); }

        // Reset all filter values
        for(var i=0; i < table.config.filter.length; i++)
          $(table.config.filter[i].filterContainer).val('').get(0).lastValue = '';

        var allRows = table.config.cache.row;

        $.tablesorter.clearTableBody(table);

        for (var i=0; i < allRows.length; i++) {
          $(table.tBodies[0]).append(allRows[i]);
        }

        $(table).trigger("update");
        $(table).trigger("appendCache");
        // Apply current sorting after restoring all rows
        $(table).trigger("sorton", [table.config.sortList]);

        if(table.config.debug) { $.tablesorter.benchmark("Clear filter:", cacheTime); }

        $(table).trigger("filterCleared");

        return table;
      };

      this.defaults = {
        filterContainer: '#filter-box',
        filterClearContainer: '#filter-clear-button',
        filterColumns: null,
        filterCaseSensitive: false,
        filterWaitTime: 500,
        filterFunction: has_words
      };


      this.construct = function() {
        // var settings = arguments; // Allow multiple config objects in constructor call
		//Daniel: Workarround for the filter issue
		//TODO: To be replaced with a better solution??
		//*********************************************************************
		//var settings = arguments;
		var settings = [];
		var argumentLen = arguments[0].length;
		if (argumentLen == undefined) {
			settings = arguments;
		}
		else {
			for (var j = 0; j < argumentLen; j++) {
				settings[j] = arguments[0][j];
			}
		}
		//*********************************************************************

        return this.each(function() {
          this.config.filter = new Array(settings.length);
          var config = this.config;
          config.filter = new Array(settings.length);

          for (var i = 0; i < settings.length; i++)
            config.filter[i] = $.extend(this.config.filter[i], $.tablesorterFilter.defaults, settings[i]);

          var table = this;

          // Create a timer which gets reset upon every keyup event.
          //
          // Perform filter only when the timer's wait is reached (user finished typing or paused long enough to elapse the timer).
          //
          // Do not perform the filter is the query has not changed.
          //
          // Immediately perform the filter if the ENTER key is pressed.

          function checkInputBox(inputBox, override) {
            var value = inputBox.value;

            if ((value != inputBox.lastValue) || (override)) {
              inputBox.lastValue = value;
              doFilter( table );
            }
          };

          var timer = new Array(settings.length);

          for (var i = 0; i < settings.length; i++) {
            var container = $(config.filter[i].filterContainer);
            // TODO: throw error for non-existing filter container?
            if(container.length)
              container[0].filterIndex = i;
            container.keyup(function(e, phrase) {
              var index = this.filterIndex;
              if(undefined !== phrase)
                $(this).val(phrase);
              var inputBox = this;

              // Was ENTER pushed?
              if (inputBox.keyCode == 13 || undefined !== phrase) {
                var timerWait = 1;
                var overrideBool = true;
              } else {
                var timerWait = config.filter[index].filterWaitTime || 500;
                var overrideBool = false;
              }

              var timerCallback = function() {
                checkInputBox(inputBox, overrideBool);
              }

              // Reset the timer
              clearTimeout(timer[index]);
              timer[index] = setTimeout(timerCallback, timerWait);

              return false;
            });

            // Avoid binding click event to whole document if no clearContainer has been defined
            if(config.filter[i].filterClearContainer) {
              var container = $(config.filter[i].filterClearContainer);
              if(container.length) {
                container[0].filterIndex = i;
                container.click(function() {
                  var index = this.filterIndex;
                  var container = $(config.filter[index].filterContainer);
                  container.val("");
                  // Support entering the same filter text after clearing
                  container[0].lastValue = "";
                  // TODO: Clear single filter only
                  doFilter(table);
                  if(container[0].type != 'hidden')
                    container.focus();
                });
              }
            }
          }

          $(table).bind("doFilter",function() {
            doFilter(table);
          });
          $(table).bind("clearFilter",function() {
            clearFilter(table);
          });
        });
      };

    }
  });

  // extend plugin scope
  $.fn.extend({
    tablesorterFilter: $.tablesorterFilter.construct
  });

})(jQuery);

/*
 * Metadata - jQuery plugin for parsing metadata from elements
 *
 * Copyright (c) 2006 John Resig, Yehuda Katz, J?örn Zaefferer, Paul McLanahan
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Revision: $Id: jquery.metadata.js 4187 2007-12-16 17:15:27Z joern.zaefferer $
 *
 */

/**
 * Sets the type of metadata to use. Metadata is encoded in JSON, and each property
 * in the JSON will become a property of the element itself.
 *
 * There are three supported types of metadata storage:
 *
 *   attr:  Inside an attribute. The name parameter indicates *which* attribute.
 *
 *   class: Inside the class attribute, wrapped in curly braces: { }
 *
 *   elem:  Inside a child element (e.g. a script tag). The
 *          name parameter indicates *which* element.
 *
 * The metadata for an element is loaded the first time the element is accessed via jQuery.
 *
 * As a result, you can define the metadata type, use $(expr) to load the metadata into the elements
 * matched by expr, then redefine the metadata type and run another $(expr) for other elements.
 *
 * @name $.metadata.setType
 *
 * @example <p id="one" class="some_class {item_id: 1, item_label: 'Label'}">This is a p</p>
 * @before $.metadata.setType("class")
 * @after $("#one").metadata().item_id == 1; $("#one").metadata().item_label == "Label"
 * @desc Reads metadata from the class attribute
 *
 * @example <p id="one" class="some_class" data="{item_id: 1, item_label: 'Label'}">This is a p</p>
 * @before $.metadata.setType("attr", "data")
 * @after $("#one").metadata().item_id == 1; $("#one").metadata().item_label == "Label"
 * @desc Reads metadata from a "data" attribute
 *
 * @example <p id="one" class="some_class"><script>{item_id: 1, item_label: 'Label'}</script>This is a p</p>
 * @before $.metadata.setType("elem", "script")
 * @after $("#one").metadata().item_id == 1; $("#one").metadata().item_label == "Label"
 * @desc Reads metadata from a nested script element
 *
 * @param String type The encoding type
 * @param String name The name of the attribute to be used to get metadata (optional)
 * @cat Plugins/Metadata
 * @descr Sets the type of encoding to be used when loading metadata for the first time
 * @type undefined
 * @see metadata()
 */

(function($) {

$.extend({
	metadata : {
		defaults : {
			type: 'class',
			name: 'metadata',
			cre: /({.*})/,
			single: 'metadata'
		},
		setType: function( type, name ){
			this.defaults.type = type;
			this.defaults.name = name;
		},
		get: function( elem, opts ){
			var settings = $.extend({},this.defaults,opts);
			// check for empty string in single property
			if ( !settings.single.length ) settings.single = 'metadata';

			var data = $.data(elem, settings.single);
			// returned cached data if it already exists
			if ( data ) return data;

			data = "{}";

			if ( settings.type == "class" ) {
				var m = settings.cre.exec( elem.className );
				if ( m )
					data = m[1];
			} else if ( settings.type == "elem" ) {
				if( !elem.getElementsByTagName )
					return undefined;
				var e = elem.getElementsByTagName(settings.name);
				if ( e.length )
					data = $.trim(e[0].innerHTML);
			} else if ( elem.getAttribute != undefined ) {
				var attr = elem.getAttribute( settings.name );
				if ( attr )
					data = attr;
			}

			if ( data.indexOf( '{' ) <0 )
			data = "{" + data + "}";

			data = eval("(" + data + ")");

			$.data( elem, settings.single, data );
			return data;
		}
	}
});

/**
 * Returns the metadata object for the first member of the jQuery object.
 *
 * @name metadata
 * @descr Returns element's metadata object
 * @param Object opts An object contianing settings to override the defaults
 * @type jQuery
 * @cat Plugins/Metadata
 */
$.fn.metadata = function( opts ){
	return $.metadata.get( this[0], opts );
};

})(jQuery);

(function($) {
	$.extend({
		tablesorterPager: new function() {

			function updatePageDisplay(c) {
				var s = $(c.cssPageDisplay,c.container).val((c.page+1) + c.seperator + c.totalPages);
			}

			function setPageSize(table,size) {
				var c = table.config;
				c.size = size;
				c.totalPages = Math.ceil(c.totalRows / c.size);
				c.pagerPositionSet = false;
				moveToPage(table);
				fixPosition(table);
                                $(table).parent().css("height", $(table).height());
			}

			function fixPosition(table) {
				var c = table.config;
				if(!c.pagerPositionSet && c.positionFixed) {
					var c = table.config, o = $(table);
					if(o.offset) {
						c.container.css({
							top: 35 + o.height() + 'px',
							position: 'absolute'
						});
					}
					c.pagerPositionSet = true;
				}
			}

			function moveToFirstPage(table) {
				var c = table.config;
				c.page = 0;
				moveToPage(table);
			}

			function moveToLastPage(table) {
				var c = table.config;
				c.page = (c.totalPages-1);
				moveToPage(table);
			}

			function moveToNextPage(table) {
				var c = table.config;
				c.page++;
				if(c.page >= (c.totalPages-1)) {
					c.page = (c.totalPages-1);
				}
				moveToPage(table);
			}

			function moveToPrevPage(table) {
				var c = table.config;
				c.page--;
				if(c.page <= 0) {
					c.page = 0;
				}
				moveToPage(table);
			}


			function moveToPage(table) {
				var c = table.config;
				if(c.page < 0 || c.page > (c.totalPages-1)) {
					c.page = 0;
				}

				renderTable(table,c.rowsCopy);
			}

			function renderTable(table,rows) {

				var c = table.config;
				var l = rows.length;
				var s = (c.page * c.size);
				var e = (s + c.size);
				if(e > rows.length ) {
					e = rows.length;
				}


				var tableBody = $(table.tBodies[0]);

				// clear the table body

				$.tablesorter.clearTableBody(table);

				for(var i = s; i < e; i++) {

					//tableBody.append(rows[i]);

					var o = rows[i];
					var l = o.length;
					for(var j=0; j < l; j++) {

						tableBody[0].appendChild(o[j]);

					}
				}

				fixPosition(table,tableBody);

				$(table).trigger("applyWidgets");

				if( c.page >= c.totalPages ) {
        			moveToLastPage(table);
				}

				updatePageDisplay(c);
			}

			this.appender = function(table,rows) {

				var c = table.config;

				c.rowsCopy = rows;
				c.totalRows = rows.length;
				c.totalPages = Math.ceil(c.totalRows / c.size);

				renderTable(table,rows);
			};

                        this.moveToPage = function(pageid, table) {
                            var c = table.config;
                            c.page = pageid - 1;
                            if(c.page < 0) c.page = 0;
                            renderTable(table,c.rowsCopy);
                        }

			this.defaults = {
				size: 10,
				offset: 0,
				page: 0,
				totalRows: 0,
				totalPages: 0,
				container: null,
				cssNext: '.next',
				cssPrev: '.prev',
				cssFirst: '.first',
				cssLast: '.last',
				cssPageDisplay: '.pagedisplay',
				cssPageSize: '.pagesize',
				seperator: "/",
				positionFixed: true,
				appender: this.appender
			};

			this.construct = function(settings) {

				return this.each(function() {

					config = $.extend(this.config, $.tablesorterPager.defaults, settings);

					var table = this, pager = config.container;

					$(this).trigger("appendCache");

					config.size = parseInt($(".pagesize",pager).val());

					$(config.cssFirst,pager).click(function() {
						moveToFirstPage(table);
						return false;
					});
					$(config.cssNext,pager).click(function() {
						moveToNextPage(table);
						return false;
					});
					$(config.cssPrev,pager).click(function() {
						moveToPrevPage(table);
						return false;
					});
					$(config.cssLast,pager).click(function() {
						moveToLastPage(table);
						return false;
					});
					$(config.cssPageSize,pager).change(function() {
						setPageSize(table,parseInt($(this).val()));
						return false;
					});
				});
			};

		}
	});
	// extend plugin scope
	$.fn.extend({
        tablesorterPager: $.tablesorterPager.construct
	});

})(jQuery);

// add parser for ducth dates (dd-mm-yyyy)
jQuery.tablesorter.addParser({
    // set a unique id
    id: 'dutchdates',
    is: function(s) {
        return false;
    },
    format: function(s) {
        s = '' + s;
        var hit = s.match(/(\d{2})-(\d{2})-(\d{4})/);
        if (hit && hit.length == 4) {
            return hit[3] + hit[2] + hit[1];
        }
        else {
            return s;
        }
    },
    type: 'text'
});

getIEVersionNumber = function() {
    var ua = navigator.userAgent;
    var MSIEOffset = ua.indexOf("MSIE ");
    if (MSIEOffset == -1) {
        return -1;
    } else {
        return parseFloat(ua.substring(MSIEOffset + 5, ua.indexOf(";", MSIEOffset)));
    }
}
var ieVersion = getIEVersionNumber();

function tablepager(tableid, tablewidth, cellheight, displayselect) {
    // Load IE6 immediatly because of positioning of elements. For the rest of the browsers: wait until DOM tree is loaded
    if (ieVersion <= 6 && ieVersion != -1) {
        tablepagerfunc(tableid, tablewidth, cellheight, displayselect);
    } else {
        jQuery(document).ready(function() {
            tablepagerfunc(tableid, tablewidth, cellheight, displayselect);
        });
    }
}

var imageurl = "/kaartenbalie/images/icons/";
var $table;
function tablepagerfunc(tableid, tablewidth, cellheight, displayselect) {

    var filters = [];
    var counter = 0;
    var cookieoptions = readCookie();
    var hasCookie = false;
    if(cookieoptions != null) hasCookie = true;

    if(displayselect == undefined) displayselect = true;

    tableid = "#" + tableid;

    $table = jQuery(tableid);
    var $parentdiv = $table.parent();

    $table.show();
    $parentdiv.css("width", tablewidth + 'px');

    // Add divs to td for overflow: hidden
    $table.find("td").each(function() {
            var curhtml = jQuery(this).html();
            jQuery(this).html('<div style="height: '+cellheight+'px; overflow: hidden;">' + curhtml + '</div>');
    });
    
    // Add extra tr for inputfilters
    var trCode = '<tr class="filterrow">';
    var filtertrigger = "";
    var addedinputfilters = false;
    $table.find("th").each(function(index) {
            if(!jQuery(this).hasClass("no-filter")) {
                    var id = "filterbox" + counter;
                    var value = "";
                    if(hasCookie && cookieoptions.filtering) {
                        jQuery.each(cookieoptions.filtering, function(index, val) {
                            if(val.col == id)
                            {
                                value = val.val;
                                if(filtertrigger != "") filtertrigger += ",";
                                filtertrigger += "#"+id;
                            }
                        });
                    }
                    trCode += '<td><input class="tablefilterbox" value="'+value+'" type="text" name="filter" id="'+id+'" /></td>';
                    id = "#" + id;
                    filters.push({
                            filterContainer: id, filterColumns: [counter]
                    });
                    addedinputfilters = true;
            } else {
                    trCode += '<td>&nbsp;</td>';
            }
            counter++;
    });
    trCode = trCode + '</th>';
    if(addedinputfilters) {
        $table.find("thead").append(trCode);
    }

    // make rows clickable & compute height of first 10 rows
    counter = 0;
    var height = $table.find("thead").outerHeight() + 4;
    $table.find("tbody > tr").each(function() {
        if(counter < 10) height += jQuery(this).outerHeight() + 2;
        jQuery(this).click(function() {
            var link = jQuery(this).find("input[name=link]").val();
            if(link != undefined && link != '') window.location.href=link;
        });
        if(jQuery(this).find("input[name=selected]").val() == "selected") {
            jQuery(this).addClass("selectedtr");
        }
        counter++;
    });

    $parentdiv.css("height", height + 'px');
    
    var $overlay = jQuery('<div id="tableoverlay"></div>').css({
        "position": "absolute",
        "background-color": "#ffffff",
        "width": tablewidth + 'px',
        "height": (height + 30) + 'px'
    });
    if(ieVersion <= 7 && ieVersion != -1) $overlay.css("height", (height + 60) + "px");
    var $insideoverlay = jQuery('<div>Bezig met laden van tabel<br /><img src="'+imageurl+'loading.gif" /></div>').css({
        "position": "absolute",
        "text-align": "center",
        "top": "50%",
        "left": "50%",
        "margin-left": "-110px",
        "margin-top": "-15px"
    });
    $overlay.append($insideoverlay);
    $table.before($overlay);

    // add pager controls
    $parentdiv.after(createPagercontrols(displayselect));  

    jQuery(".refreshtable").click(function() {
        resetFilters(tableid);
    })

    // init tablesorter
    $table.tablesorter({
            widthFixed: true,
            widgets: ['zebra'],
            textExtraction: function(node) {
                    return node.childNodes[0].innerHTML;
            }
    });

    $table.bind("sortEnd",function() {
        storeCookie(tableid);
    });
    if(addedinputfilters) {
        $table.find(".tablefilterbox").keyup(function() {
            storeCookie(tableid);
        });
    }

    // init filter
    $table.tablesorterFilter(filters);
    if(filtertrigger != "") $table.trigger("doFilter");
    if(hasCookie && cookieoptions.sorting) $table.trigger("sorton",[[cookieoptions.sorting]]);

    // init pager
    $table.tablesorterPager({container: jQuery("#pager"), positionFixed: false, page: 0});

    // set input widths
    $table.find(".filterrow td").each(function(index) {
        var inputwidth = jQuery(this).width() - 8;
        jQuery(this).find("input").css("width", inputwidth + 'px');
    });

    setTimeout(function() { moveToSelectedPage(); jQuery("#tableoverlay").remove(); }, 1000);
}

function moveToSelectedPage() {
    var counter = 1;
    var childnr = 0;
    var $rows = jQuery($table[0].config.rowsCopy);
    $rows.each(function(){
        if(jQuery(this).find("input[name=selected]").val() == "selected") {
            childnr = counter;
        }
        counter++;
    });

    var selectedpage = Math.ceil(childnr / 10);
    if(selectedpage == 0) selectedpage = 1;

    jQuery.tablesorterPager.moveToPage(selectedpage,$table[0]);
}

function resetFilters(tableid) {
    var $table = jQuery(tableid);
    $table.trigger("sorton",[[]]);
    $table.find(".tablefilterbox").attr("value", "");
    $table.find(".tablefilterbox").trigger("keyup");
    jQuery(".first").trigger("click");
    deleteCookie();
}

function deleteCookie() {
    jQuery.cookie('sortfiltercookie', null);
}

var pagepath = location.pathname.replace("-", "");
function storeCookie(tableid) {
    // cookievalue sort-filter
    // example: pagename-page:sort:colid:up/down-filtering:colname*value**colname*value**colname*value
    var counter = 0;
    var sorttext = "";
    jQuery(tableid + " thead tr:first").find("th").each(function() {
        if(sorttext == "") {
            if(jQuery(this).hasClass("headerSortDown")) {
                sorttext += counter + ":0";
            }
            if(jQuery(this).hasClass("headerSortUp")) {
                sorttext += counter + ":1";
            }
        }
        counter++;
    });

    var filtertext = "";
    jQuery(tableid + " thead").find(".tablefilterbox").each(function() {
        if(jQuery(this).val() != '') {
            if(filtertext != "") filtertext += "**";
            var val = jQuery(this).val().replace("**", "####");
            val = val.replace("*", "##");
            filtertext += jQuery(this).attr("id") + "*" + val;
        }
    });

    var cookietext = "pagename:" + pagepath;
    if(sorttext != "") {
        if(cookietext != "") cookietext += "-";
        cookietext += "sort:" + sorttext;
    }
    if(filtertext != "") {
        if(cookietext != "") cookietext += "-";
        cookietext +=  "filtering:" + filtertext;
    }
    jQuery.cookie('sortfiltercookie', cookietext);
}

var returnmap = {};
var cookietekstgelezen = "";
function readCookie() {
    // cookievalue sort-filter
    // example: pagename:page-colname:up/down-colname*value**colname*value**colname*value

    var sfcookie = jQuery.cookie('sortfiltercookie');
    cookietekstgelezen = sfcookie;
    if(sfcookie == null) return sfcookie;

    var cookiearray = sfcookie.split("-");

    var sortarray = null;
    var filterarray = null;
    var returnnull = false;
    jQuery.each(cookiearray, function(index, value) {
        var splitarray = value.split(":");
        if(splitarray[0] == "pagename" && splitarray[1] != pagepath) returnnull = true;
        if(splitarray[0] == "sort") sortarray = splitarray;
        if(splitarray[0] == "filtering") filterarray = splitarray[1].split("**");
    });

    if(returnnull) {
        deleteCookie();
        return null;
    }
    if(sortarray != null) returnmap["sorting"] = [parseInt(sortarray[1]),parseInt(sortarray[2])];
    if(filterarray != null) {
        returnmap["filtering"] = [];
        jQuery.each(filterarray, function (index, value){
            var colarray = value.split("*");
            var val = colarray[1].replace("####", "**");
            val = val.replace("##", "*");
            returnmap["filtering"].push({col:colarray[0], val:val});
        });
    }

    return returnmap;
}

function createPagercontrols(displayselect) {
    var controls = '<div id="pager" class="pager" style="padding-top: 3px;">' +
            '<form>' +
                    '<img src="'+imageurl+'first.png" class="first"/> ' +
                    '<img src="'+imageurl+'prev.png" class="prev"/> ' +
                    '<input type="text" class="pagedisplay"/> ' +
                    '<img src="'+imageurl+'next.png" class="next"/> ' +
                    '<img src="'+imageurl+'last.png" class="last"/> ' +
                    '<img src="'+imageurl+'refresh.png" class="refreshtable"/> ';
                if(displayselect) {
                    controls += '<select class="pagesize">' +
                            '<option selected="selected" value="10">10</option>' +
                            '<option value="20">20</option>' +
                            '<option value="30">30</option>' +
                            '<option  value="40">40</option>' +
                    '</select>';
                } else {
                    controls += '<input type="hidden" class="pagesize" value="10" />';
                }
            '</form>' +
    '</div>';
    return controls;
}