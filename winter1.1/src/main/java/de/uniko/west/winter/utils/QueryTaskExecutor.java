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

import java.util.LinkedList;
import java.util.Queue;

import de.uniko.west.winter.utils.interfaces.QueryResultListener;
import de.uniko.west.winter.utils.interfaces.RepositoryConnection;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 */
public class QueryTaskExecutor{
	
	private static QueryTaskExecutor sharedInstance = null;
	
	public static QueryTaskExecutor getSharedInstance() {
		if (sharedInstance == null){
			sharedInstance = new QueryTaskExecutor(); 
		}
		return sharedInstance;
	}
	
	Queue<QueryTask> queryTasks = null;
	
	private QueryTaskExecutor(){
		queryTasks = new LinkedList<QueryTask>();
	}
	
	public void queueTask(QueryTask queryTask) {
		queryTasks.add(queryTask);
	}
	
	public synchronized void runTasks(RepositoryConnection<?> repCon, QueryResultListener listener) {
		// TODO Threading
		while (!queryTasks.isEmpty()) {
			queryTasks.poll().execute(repCon, listener);
		}
	}

}
