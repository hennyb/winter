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

package de.uniko.west.winter.utils.interfaces;

import de.uniko.west.winter.core.QueryResult;
import de.uniko.west.winter.utils.DefaultQueryTask;
import de.uniko.west.winter.utils.DeleteDataQueryTask;
import de.uniko.west.winter.utils.InsertDataQueryTask;
import de.uniko.west.winter.utils.SelectQueryTask;

public interface RepositoryConnection<T> {
	
	public void connectRepository (T repCon);

	public T getRepository ();
	
	public void setRepository (T repCon);
	
	public QueryResult evaluateQuery(DefaultQueryTask queryTask);

	public QueryResult evaluateQuery(InsertDataQueryTask queryTask);
	
	public QueryResult evaluateQuery(SelectQueryTask queryTask);
	
	public QueryResult evaluateQuery(DeleteDataQueryTask queryTask);
		
//	public String toJsonResult(Object resultObject) throws UnsupportedEncodingException;
}
