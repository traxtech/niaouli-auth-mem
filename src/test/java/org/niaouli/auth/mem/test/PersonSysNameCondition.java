/**
 * This file is part of Niaouli Auth Mem.
 *
 * Niaouli Auth Mem is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Niaouli Auth Mem is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Niaouli Auth Mem. If not, see <http://www.gnu.org/licenses/>.
 */
package org.niaouli.auth.mem.test;

import org.assertj.core.api.Condition;
import org.niaouli.auth.Person;

/**
 *
 * @author Arnaud Rolly <github@niaouli.org>
 */
public class PersonSysNameCondition extends Condition<Person> {

    private final String sysName;

    public PersonSysNameCondition(String pSysName) {
        sysName = pSysName;
    }

    @Override
    public boolean matches(Person t) {
        return (sysName == null && t.getSysName() == null) || sysName.equals(t.getSysName());
    }

}
