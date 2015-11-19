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

import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.niaouli.auth.Person;
import org.niaouli.auth.mem.MemAuthSystem;
import org.niaouli.exception.AppException;

/**
 *
 * @author Arnaud Rolly <github@niaouli.org>
 */
public class PersonTest {

    private static final String JOHN = "JOHN";
    private static final String LUC = "LUC";
    private static final char[] PWD1 = "x!m141M".toCharArray();
    private static final char[] PWD2 = "POOl::k".toCharArray();
    private static final char[] EMPTY = new char[]{};

    @Test
    public void testHealth() throws AppException {
        MemAuthSystem memAuthSystem = new MemAuthSystem();
        assertThat(memAuthSystem.checkHealth()).isTrue();
    }

    @Test
    public void testNominal() throws AppException {
        MemAuthSystem memAuthSystem = new MemAuthSystem();
        assertThat(memAuthSystem.canCreateOrUpdatePerson()).isTrue();
        Person person = new Person();
        person.setSysName(JOHN);
        memAuthSystem.createPerson(person);
        person = memAuthSystem.loadPerson(JOHN);
        assertThat(person).isNotNull();
        assertThat(memAuthSystem.findPersons()).are(new PersonSysNameCondition(JOHN));
    }

    @Test
    public void testMultiple() throws AppException {
        MemAuthSystem memAuthSystem = new MemAuthSystem();
        assertThat(memAuthSystem.canCreateOrUpdatePerson()).isTrue();
        Person person = new Person();
        person.setSysName(JOHN);
        memAuthSystem.createPerson(person);
        person = new Person();
        person.setSysName(LUC);
        memAuthSystem.createPerson(person);
        Collection<Person> allPersons = memAuthSystem.findPersons();
        assertThat(allPersons).hasSize(2);
        assertThat(allPersons).areExactly(1, new PersonSysNameCondition(JOHN));
        assertThat(allPersons).areExactly(1, new PersonSysNameCondition(LUC));
    }

    @Test(expected = AppException.class)
    public void testDuplicate() throws AppException {
        MemAuthSystem memAuthSystem = new MemAuthSystem();
        Person person = new Person();
        person.setSysName(JOHN);
        memAuthSystem.createPerson(person);
        memAuthSystem.createPerson(person);
    }

    @Test
    public void testPasswordNominal() throws AppException {
        MemAuthSystem memAuthSystem = new MemAuthSystem();
        assertThat(memAuthSystem.canCreateOrUpdatePerson()).isTrue();
        Person person = new Person();
        person.setSysName(JOHN);
        memAuthSystem.createPerson(person);
        assertThat(memAuthSystem.canUpdatePassword()).isTrue();
        memAuthSystem.updatePersonPassword(JOHN, PWD1);
        assertThat(memAuthSystem.checkCredentials(JOHN, PWD1)).isTrue();
        assertThat(memAuthSystem.checkCredentials(JOHN, PWD2)).isFalse();
        memAuthSystem.updatePersonPassword(JOHN, PWD2);
        assertThat(memAuthSystem.checkCredentials(JOHN, PWD1)).isFalse();
        assertThat(memAuthSystem.checkCredentials(JOHN, PWD2)).isTrue();
    }

    @Test(expected = AppException.class)
    public void testPasswordNoPerson() throws AppException {
        MemAuthSystem memAuthSystem = new MemAuthSystem();
        memAuthSystem.updatePersonPassword(JOHN, PWD1);
    }

    public void testCheckCredentials() throws AppException {
        MemAuthSystem memAuthSystem = new MemAuthSystem();
        Person person = new Person();
        person.setSysName(JOHN);
        memAuthSystem.createPerson(person);
        assertThat(memAuthSystem.checkCredentials(null, null)).isFalse();
        assertThat(memAuthSystem.checkCredentials("", null)).isFalse();
        assertThat(memAuthSystem.checkCredentials(null, EMPTY)).isFalse();
        assertThat(memAuthSystem.checkCredentials("", EMPTY)).isFalse();
        assertThat(memAuthSystem.checkCredentials(JOHN, null)).isFalse();
        assertThat(memAuthSystem.checkCredentials(JOHN, EMPTY)).isFalse();
        memAuthSystem.updatePersonPassword(JOHN, PWD1);
        assertThat(memAuthSystem.checkCredentials(JOHN, null)).isFalse();
        assertThat(memAuthSystem.checkCredentials(JOHN, EMPTY)).isFalse();
        assertThat(memAuthSystem.checkCredentials(JOHN, PWD2)).isFalse();
        assertThat(memAuthSystem.checkCredentials(JOHN, PWD1)).isTrue();
    }
}
