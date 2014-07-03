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

import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.uniko.west.winter.utils.interfaces.RepositoryConnection;
import de.uniko.west.winter.utils.repcon.JenaRepositoryConnection;

public class JenaInit extends Init {
	
	public JenaInit(){
		this( new JenaRepositoryConnection(ModelFactory.createDefaultModel()) );
	}
	
	public JenaInit(RepositoryConnection repCon){
		this.repCon = repCon;
	}
	
	public JenaInit(RepositoryConnection repCon, Map<String, URI> prefixMap){
		this.repCon = repCon;
		this.prefixMap = prefixMap;		
	}
	
	@Override
	public RepositoryConnection getRepositoryConnection(){
		return this.repCon;
	}
	
	@Override
	public Map<String, URI> getPrefixMap(){
		return this.prefixMap;
	}
}
