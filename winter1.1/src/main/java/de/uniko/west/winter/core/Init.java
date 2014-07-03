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

package de.uniko.west.winter.core;

import java.net.URI;
import java.util.Map;

import de.uniko.west.winter.utils.interfaces.RepositoryConnection;

public abstract class Init{
	
	protected RepositoryConnection repCon;
	protected Map<String, URI> prefixMap;
	
	public Init(){
		
	}
	
	public Init(RepositoryConnection repCon){
		this.repCon = repCon;
	}
	
	public Init(RepositoryConnection repCon, Map<String, URI> prefixMap){
		this.repCon = repCon;
		prefixMap = prefixMap;		
	}
	
	public RepositoryConnection getRepositoryConnection(){
		return this.repCon;
	}
	
	public Map<String, URI> getPrefixMap(){
		return this.prefixMap;
	}
}
