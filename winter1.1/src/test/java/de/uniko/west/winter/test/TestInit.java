/*

The WINTER-API is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

The WINTER-API is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with the WINTER-API.  If not, see <http://www.gnu.org/licenses/>.

*/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniko.west.winter.test;

import java.io.File;
import java.net.URI;
import java.util.Map;

import de.uniko.west.winter.core.Init;
import de.uniko.west.winter.utils.interfaces.RepositoryConnection;

/**
 * The sesame http memory init. Initializing http store, sail repository connection and value factory 
 * @author Stefan Scheglmann, Frederik Jochum
 */
public class TestInit extends Init {
   
	RepositoryConnection repCon;
	Map<String, URI> prefixMap;
	
	public TestInit(RepositoryConnection repCon) {
		this.repCon = repCon;
	}
	
	public TestInit(RepositoryConnection repCon, Map<String, URI> prefixMap) {
		this.repCon = repCon;
		this.prefixMap = prefixMap;
	}
	
	public RepositoryConnection getRepositoryConnection(){
		return this.repCon;
	}
	
	public Map<String, URI> getPrefixMap() {
		return this.prefixMap;
	}
}
