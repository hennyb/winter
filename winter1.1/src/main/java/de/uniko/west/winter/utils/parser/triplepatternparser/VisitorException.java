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

package de.uniko.west.winter.utils.parser.triplepatternparser;

public class VisitorException extends Exception {

    public VisitorException() {
        super();
    }

    public VisitorException(String msg) {
        super(msg);
    }

    public VisitorException(String msg, Throwable t) {
        super(msg, t);
    }

    public VisitorException(Throwable t) {
        super(t);
    }

}
