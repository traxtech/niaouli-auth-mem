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
import org.junit.Before;
import org.junit.Test;
import org.niaouli.auth.Person;
import org.niaouli.auth.PersonBuilder;
import org.niaouli.auth.mem.MemAuthSystem;
import org.niaouli.exception.AppException;

/**
 *
 * @author Arnaud Rolly <github@niaouli.org>
 */
public class PersonTest {

    private static final String JOHN = "JOHN";
    private static final String LUC = "LUC";
    private static final String FULLNAME = "Someone's name";

    private static final char[] PWD1 = "x!m141M".toCharArray();
    private static final char[] PWD2 = "POOl::k".toCharArray();
    private static final char[] EMPTY = new char[]{};

    private MemAuthSystem authSystem;

    @Before
    public void before() {
        authSystem = new MemAuthSystem();
    }

    @Test
    public void testHealth() throws AppException {
        assertThat(authSystem.checkHealth()).isTrue();
    }

    @Test
    public void testNominal() throws AppException {
        assertThat(authSystem.canCreateOrUpdatePerson()).isTrue();
        createPerson(JOHN);
        Person person = authSystem.loadPerson(JOHN);
        assertThat(person).isNotNull();
        assertThat(authSystem.findPersons()).are(new PersonSysNameCondition(JOHN));
    }

    @Test
    public void testMultiple() throws AppException {
        assertThat(authSystem.canCreateOrUpdatePerson()).isTrue();
        createPerson(JOHN);
        createPerson(LUC);
        Collection<Person> allPersons = authSystem.findPersons();
        assertThat(allPersons).hasSize(2);
        assertThat(allPersons).areExactly(1, new PersonSysNameCondition(JOHN));
        assertThat(allPersons).areExactly(1, new PersonSysNameCondition(LUC));
    }

    @Test(expected = AppException.class)
    public void testDuplicate() throws AppException {
        createPerson(JOHN);
        createPerson(JOHN);
    }

    @Test
    public void testUpdate() throws AppException {
        createPerson(JOHN);
        PersonBuilder builder = new PersonBuilder(authSystem.loadPerson(JOHN));
        builder.setFullName(FULLNAME);
        authSystem.updatePerson(builder.build());
        assertThat(authSystem.loadPerson(JOHN).getFullName()).isEqualTo(FULLNAME);
    }

    @Test
    public void testPasswordNominal() throws AppException {
        createPerson(JOHN);
        assertThat(authSystem.canUpdatePassword()).isTrue();
        authSystem.updatePersonPassword(JOHN, PWD1);
        assertThat(authSystem.checkCredentials(JOHN, PWD1)).isTrue();
        assertThat(authSystem.checkCredentials(JOHN, PWD2)).isFalse();
        authSystem.updatePersonPassword(JOHN, PWD2);
        assertThat(authSystem.checkCredentials(JOHN, PWD1)).isFalse();
        assertThat(authSystem.checkCredentials(JOHN, PWD2)).isTrue();
    }

    @Test(expected = AppException.class)
    public void testPasswordNoPerson() throws AppException {
        authSystem.updatePersonPassword(JOHN, PWD1);
    }

    public void testCheckCredentials() throws AppException {
        createPerson(JOHN);
        assertThat(authSystem.checkCredentials(null, null)).isFalse();
        assertThat(authSystem.checkCredentials("", null)).isFalse();
        assertThat(authSystem.checkCredentials(null, EMPTY)).isFalse();
        assertThat(authSystem.checkCredentials("", EMPTY)).isFalse();
        assertThat(authSystem.checkCredentials(JOHN, null)).isFalse();
        assertThat(authSystem.checkCredentials(JOHN, EMPTY)).isFalse();
        authSystem.updatePersonPassword(JOHN, PWD1);
        assertThat(authSystem.checkCredentials(JOHN, null)).isFalse();
        assertThat(authSystem.checkCredentials(JOHN, EMPTY)).isFalse();
        assertThat(authSystem.checkCredentials(JOHN, PWD2)).isFalse();
        assertThat(authSystem.checkCredentials(JOHN, PWD1)).isTrue();
    }

    private void createPerson(String sysName) throws AppException {
        PersonBuilder builder = new PersonBuilder();
        builder.setSysName(sysName);
        authSystem.createPerson(builder.build());
    }
}
