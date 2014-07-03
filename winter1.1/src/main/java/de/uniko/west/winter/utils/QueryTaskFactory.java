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

/**
 * 
 */
package de.uniko.west.winter.utils;

import java.net.URI;
import java.util.Map;

import de.uniko.west.winter.infostructure.InfoNode;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class QueryTaskFactory {
	
	public static enum TASK {
		INSERT_DATA,
		DELETE_DATA,
		SELECT;
	}
	
	private static QueryTaskExecutor executor;
	
	public static QueryTask createQuery(InfoNode node, URI graph, Map<String, URI> prefixMap, TASK task, String ... var){
		DefaultQueryTask queryTask = null;
		
		switch (task) {
		case INSERT_DATA:
			queryTask = new InsertDataQueryTask(node, graph, prefixMap, executor, var);
			break;
		case DELETE_DATA:
			queryTask = new DeleteDataQueryTask(node, graph, prefixMap, executor, var);
			break;
		case SELECT:
			queryTask = new SelectQueryTask(node, graph, prefixMap, executor, var);

			break;

		default:
//			TODO throw something
			return null;
		}
		queryTask.insertTask();
		queryTask.insertPrefix();
		
		return queryTask;
	}
	
	
}
