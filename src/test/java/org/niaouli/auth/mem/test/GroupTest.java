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
import org.niaouli.auth.Group;
import org.niaouli.auth.GroupBuilder;
import org.niaouli.auth.PersonBuilder;
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
    private static final String AUDIT = "AUDIT";

    private static final String DESCRIPTION = "Something here...";

    private MemAuthSystem authSystem;

    @Before
    public void before() {
        authSystem = new MemAuthSystem();
    }

    @Test
    public void testNominal() throws AppException {
        assertThat(authSystem.canCreateOrUpdateGroup()).isTrue();
        createGroup(IT);
        Group it = authSystem.loadGroup(IT);
        assertThat(it).isNotNull();
        assertThat(authSystem.findGroups()).are(new GroupSysNameCondition(IT));
    }

    @Test
    public void testMultiple() throws AppException {
        createGroup(IT);
        createGroup(AUDIT);
        Collection<Group> allGroups = authSystem.findGroups();
        assertThat(allGroups).hasSize(2);
        assertThat(allGroups).areExactly(1, new GroupSysNameCondition(IT));
        assertThat(allGroups).areExactly(1, new GroupSysNameCondition(AUDIT));
    }

    @Test(expected = AppException.class)
    public void testDuplicate() throws AppException {
        createGroup(IT);
        createGroup(IT);
    }

    @Test
    public void testUpdate() throws AppException {
        createGroup(IT);
        GroupBuilder builder = new GroupBuilder(authSystem.loadGroup(IT));
        builder.setDescription(DESCRIPTION);
        authSystem.updateGroup(builder.build());
        assertThat(authSystem.loadGroup(IT).getDescription()).isEqualTo(DESCRIPTION);
    }

    @Test
    public void testAttachPerson() throws AppException {
        createGroup(IT);
        createGroup(AUDIT);
        createPerson(JOHN);
        createPerson(LUC);

        assertThat(authSystem.findGroupPersons(IT)).isEmpty();
        authSystem.attachGroupMember(IT, JOHN);
        assertThat(authSystem.findGroupPersons(IT)).containsExactly(JOHN);

        assertThat(authSystem.findGroupPersons(AUDIT)).isEmpty();
        authSystem.attachGroupMember(AUDIT, JOHN);
        assertThat(authSystem.findGroupPersons(AUDIT)).containsExactly(JOHN);
        authSystem.attachGroupMember(AUDIT, LUC);
        assertThat(authSystem.findGroupPersons(AUDIT)).containsExactly(JOHN, LUC);
    }

    @Test
    public void testDetachPerson() throws AppException {
        createGroup(IT);
        createPerson(JOHN);
        createPerson(LUC);
        authSystem.attachGroupMember(IT, JOHN);
        authSystem.attachGroupMember(IT, LUC);
        assertThat(authSystem.findGroupPersons(IT)).containsExactly(JOHN, LUC);
        authSystem.detachGroupMember(IT, LUC);
        assertThat(authSystem.findGroupPersons(IT)).containsExactly(JOHN);
        authSystem.detachGroupMember(IT, JOHN);
        assertThat(authSystem.findGroupPersons(IT)).isEmpty();

    }

    private void createGroup(String sysName) throws AppException {
        GroupBuilder builder = new GroupBuilder();
        builder.setSysName(sysName);
        authSystem.createGroup(builder.build());
    }

    private void createPerson(String sysName) throws AppException {
        PersonBuilder builder = new PersonBuilder();
        builder.setSysName(sysName);
        authSystem.createPerson(builder.build());
    }

}
