/*
 * B3P Kaartenbalie is a OGC WMS/WFS proxy that adds functionality
 * for authentication/authorization, pricing and usage reporting.
 *
 * Copyright 2006, 2007, 2008 B3Partners BV
 * 
 * This file is part of B3P Kaartenbalie.
 * 
 * B3P Kaartenbalie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * B3P Kaartenbalie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with B3P Kaartenbalie.  If not, see <http://www.gnu.org/licenses/>.
 */

function checkAll(ref, chkAll) {
    var checks = document.getElementsByName('deleteReport');
    var removeButton = document.getElementById('removeChecked');
    var boxLength = checks.length;
    var allChecked = false;
    var totalChecked = 0;
    if ( ref == 1 ) {
        if ( chkAll.checked == true ) {
            for ( i=0; i < boxLength; i++ ) {
                checks[i].checked = true;
            }
        }
        else {
            for ( i=0; i < boxLength; i++ ) {
                checks[i].checked = false;
            }
        }
    }
    else {
        for ( i=0; i < boxLength; i++ ) {
            if ( checks[i].checked == true ) {
                allChecked = true;
                continue;
            }
            else {
                allChecked = false;
                break;
            }
        }
        if ( allChecked == true ) {
            chkAll.checked = true;
        }
        else {
            chkAll.checked = false;
        }
    }
    for ( j=0; j < boxLength; j++ ) {
        if ( checks[j].checked == true ) {
            totalChecked++;
	}
    }
    if(totalChecked > 0)
        removeButton.value = "Verwijder "+totalChecked+" geselecteerden";
    else 
        removeButton.value = "Verwijder geselecteerd";
}