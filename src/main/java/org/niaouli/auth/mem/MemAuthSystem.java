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
package org.niaouli.auth.mem;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.niaouli.auth.AuthSystem;
import org.niaouli.auth.Group;
import org.niaouli.auth.OrgUnit;
import org.niaouli.auth.Person;

/**
 *
 * @author Arnaud Rolly <github@niaouli.org>
 */
public class MemAuthSystem implements AuthSystem, Serializable {

    private final Map<String, Person> persons = new HashMap<>();
    private final Map<String, Group> groups = new HashMap<>();
    private final Map<String, OrgUnit> orgUnits = new HashMap<>();
    private final Map<String, char[]> passwords = new HashMap<>();

    @Override
    public void configure(Map<String, String> props) {
    }

    @Override
    public boolean checkHealth() {
        return true;
    }

    @Override
    public boolean checkCredentials(String sysName, char[] password) {
        return passwords.containsKey(sysName) && Arrays.equals(passwords.get(sysName), password);
    }

    @Override
    public Person loadPerson(String sysName) {
        if (sysName == null || !persons.containsKey(sysName)) {
            throw new RuntimeException("Invalid person system");
        }
        return persons.get(sysName);
    }

    @Override
    public Collection<Person> findPersons() {
        return Collections.unmodifiableCollection(persons.values());
    }

    @Override
    public boolean canCreateOrUpdatePerson() {
        return true;
    }

    @Override
    public boolean canUpdatePassword() {
        return true;
    }

    @Override
    public void createPerson(Person person) {
        updatePerson(person);
    }

    @Override
    public void updatePerson(Person person) {
        if (person.getSysName() == null || persons.containsKey(person.getSysName())) {
            throw new RuntimeException("Invalid or already used system name");
        }
        persons.put(person.getSysName(), person);
    }

    @Override
    public void updatePersonPassword(String sysName, char[] password) {
        if (sysName == null || !persons.containsKey(sysName)) {
            throw new RuntimeException("Invalid or already used system name");
        }
        passwords.put(sysName, password);
    }

    @Override
    public Group loadGroup(String sysName) {
        if (sysName == null || !groups.containsKey(sysName)) {
            throw new RuntimeException("Invalid person system");
        }
        return groups.get(sysName);
    }

    @Override
    public Collection<Group> findGroups() {
        return Collections.unmodifiableCollection(groups.values());
    }

    @Override
    public boolean canCreateOrUpdateGroup() {
        return true;
    }

    @Override
    public void createGroup(Group group) {
        updateGroup(group);
    }

    @Override
    public void updateGroup(Group group) {
        if (group.getSysName() == null || groups.containsKey(group.getSysName())) {
            throw new RuntimeException("Invalid or already used system name");
        }
        groups.put(group.getSysName(), group);
    }

    @Override
    public OrgUnit loadOrgUnit(String name) {
        if (name == null || !orgUnits.containsKey(name)) {
            throw new RuntimeException("Invalid person system");
        }
        return orgUnits.get(name);
    }

    @Override
    public Collection<OrgUnit> findOrgUnits() {
        return Collections.unmodifiableCollection(orgUnits.values());
    }

    @Override
    public boolean canCreateOrUpdateOrgUnit() {
        return true;
    }

    @Override
    public void createOrgUnit(OrgUnit orgUnit) {
        updateOrgUnit(orgUnit);
    }

    @Override
    public void updateOrgUnit(OrgUnit orgUnit) {
        if (orgUnit.getName() == null || groups.containsKey(orgUnit.getName())) {
            throw new RuntimeException("Invalid or already used name");
        }
        orgUnits.put(orgUnit.getName(), orgUnit);
    }

}
