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

package de.uniko.west.winter.core.serializing;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniko.west.winter.exceptions.WinterException;

//still use this?
// - maybe for custom requests...
@Deprecated
public class JSONResultProcessor {
	
	protected static transient Logger logger = LoggerFactory.getLogger(JSONResultProcessor.class.toString());
	
	private static final String KEY_HEAD = "head";
	private static final String KEY_RESULTS = "results";
	private static final String KEY_VARS = "vars";
	private static final String KEY_BINDINGS = "bindings";
	
	
	public static Iterator<Object> processResultsSingleVar(String results, String var){
		
		try {
			System.out.println("RESULT: " + results);
			JSONObject jsonResultObject = new JSONObject(results);
			System.out.println("JSONRESULT: " + jsonResultObject);
			Map<String, Set<Object>> resultsMap = convertJson2Map(jsonResultObject);
			if (resultsMap.containsKey(var)){
				return resultsMap.get(var).iterator();
			}else{
				throw new WinterException("Exception while updateprocess. Variable not in query result.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static Map<String, Set<Object>> processResultsMultiVarFilter(String results, Set<String> varFilter){
		
		try {
			JSONObject jsonResultObject = new JSONObject(results);
			Map<String, Set<Object>> resultsMap = convertJson2Map(jsonResultObject);
			for (String key : resultsMap.keySet()) {
				if (!varFilter.contains(key)){
					resultsMap.remove(key);
				}
			}
			return resultsMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Map<String, Set<Object>> convertJson2Map(JSONObject jsonResultObj)
			throws WinterException {

		JSONObject headObj = null;
		JSONObject resultObj = null;
		JSONArray vars = null;
		JSONArray results = null;

		try {
			headObj = jsonResultObj.getJSONObject(KEY_HEAD);
			resultObj = jsonResultObj.getJSONObject(KEY_RESULTS);
			vars = headObj.getJSONArray(KEY_VARS);
			results = resultObj.getJSONArray(KEY_BINDINGS);

			if (headObj == null || resultObj == null || vars == null
					|| results == null) {
				throw new NullPointerException(
						"Essential elements in JSONObject are null!");
			}
		} catch (Exception e) {
			throw new WinterException(
					"Missing essential elements in JSON result object!", e);
		}
		
		Map<String, Set<Object>> bindingMap = new HashMap<String, Set<Object>>();

		for (int j = 0; j < results.length(); j++) {
			for (int i = 0; i < vars.length(); i++) {
				Object value = null;
				String key = null;
				try {
					key = vars.getString(i);
					if (!bindingMap.containsKey(key)){
						bindingMap.put(key, new HashSet<Object>());
					}


					
					String valueString = results.getJSONObject(j)
							.getJSONObject(key).getString("value");
					String type = results.getJSONObject(j).getJSONObject(key)
							.getString("type");
					

					if (type.equalsIgnoreCase("uri")) {
						value = new URI(valueString);
					} else if (type.equalsIgnoreCase("literal")) {
						value = valueString;
					} else if (type.equalsIgnoreCase("typed-literal")) {

						URI typeURI = new URI(results.getJSONObject(j)
								.getJSONObject(key).getString("datatype"));
						String datatype = ((String) typeURI.getPath()
								.substring(
										typeURI.getPath().lastIndexOf("/") + 1))
								.trim();

						// Trying java primitives
						// creating new primitive wrapper object by using its constructor with String parameter
						// e.g. Integer: value = new Integer(valueString);
						value = Class.forName(
								"java.lang."
										+ datatype.substring(0, 1)
												.toUpperCase()
										+ datatype.substring(1).toLowerCase(),
								true, ClassLoader.getSystemClassLoader())
								.getConstructor(String.class).newInstance(
										valueString);

					} else {
						throw new WinterException("Wrong Type");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				if ( key!=null && value!=null ){
					bindingMap.get(key).add(value);
				}

			}

		}

//		remove entries with empty set
		for (int i = 0; i < bindingMap.size(); i++) {
			Object key = bindingMap.keySet().toArray()[i];
			if ( bindingMap.get( key ).isEmpty()){
				bindingMap.remove(key);
				i--;
			}
		}
		
		return bindingMap;
	}

}
