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

package de.uniko.west.winter.annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Stefan Scheglmann, Frederik Jochum
 *
 * With this annotation relation objects are annotated with a SPARQL-Query-PAttern
 * this enables Winter to Map relationships defined between <code>Field</code> in
 * the same class.
 * Every variable present in this query should correlate with a <code>@winter_var</code> or a
 * <code>@winter_ref</code> annotated <code>Field</code> in this class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
public @interface winter_query {
	String value();
}
