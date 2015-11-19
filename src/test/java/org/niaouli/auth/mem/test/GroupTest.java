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

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.niaouli.auth.Group;
import org.niaouli.auth.Person;
import org.niaouli.auth.mem.MemAuthSystem;
import org.niaouli.exception.AppException;

/**
 *
 * @author Arnaud Rolly <github@niaouli.org>
 */
public class GroupTest {

    private static final String JOHN = "JOHN";
    private static final String LUC = "LUC";

    private static final String IT = "IT";

    @Test
    public void testNominal() throws AppException {
        MemAuthSystem memAuthSystem = new MemAuthSystem();
        assertThat(memAuthSystem.canCreateOrUpdateGroup()).isTrue();
        Person person = new Person();
        person.setSysName(JOHN);
        memAuthSystem.createPerson(person);
        person = new Person();
        person.setSysName(LUC);
        memAuthSystem.createPerson(person);

        Group it = new Group();
        it.setSysName(IT);
        memAuthSystem.createGroup(it);
        it = memAuthSystem.loadGroup(IT);
        assertThat(it).isNotNull();
        assertThat(memAuthSystem.findGroups()).are(new GroupSysNameCondition(IT));
    }
}
