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
import org.niaouli.exception.AppException;
import org.niaouli.validation.Validation;

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
    public boolean checkCredentials(String sysName, char[] password) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(sysName).inField("sysName").isNotEmpty();
        validation.finish();
        return passwords.containsKey(sysName) && Arrays.equals(passwords.get(sysName), password);
    }

    @Override
    public Person loadPerson(String sysName) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(sysName).inField("sysName")
                .isNotEmpty()
                .isInMapKeys(persons);
        validation.finish();
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
    public void createPerson(Person person) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(person.getSysName()).inField("person.sysName")
                .isNotEmpty()
                .isNotInMapKeys(persons);
        validation.finish();
        persons.put(person.getSysName(), person);
    }

    @Override
    public void updatePerson(Person person) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(person.getSysName()).inField("person.sysName")
                .isNotEmpty()
                .isInMapKeys(persons);
        validation.finish();
        persons.put(person.getSysName(), person);
    }

    @Override
    public void updatePersonPassword(String sysName, char[] password) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(sysName).inField("sysName")
                .isNotEmpty()
                .isInMapKeys(persons);
        validation.finish();
        passwords.put(sysName, password);
    }

    @Override
    public Group loadGroup(String sysName) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(sysName).inField("sysName")
                .isNotEmpty()
                .isInMapKeys(groups);
        validation.finish();
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
    public void createGroup(Group group) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(group.getSysName()).inField("group.sysName")
                .isNotEmpty()
                .isNotInMapKeys(groups);
        validation.finish();
        groups.put(group.getSysName(), group);
    }

    @Override
    public void updateGroup(Group group) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(group.getSysName()).inField("group.sysName")
                .isNotEmpty()
                .isInMapKeys(groups);
        validation.finish();
        groups.put(group.getSysName(), group);
    }

    @Override
    public OrgUnit loadOrgUnit(String name) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(name).inField("name")
                .isNotEmpty()
                .isInMapKeys(orgUnits);
        validation.finish();
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
    public void createOrgUnit(OrgUnit orgUnit) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(orgUnit.getName()).inField("orgUnit.name")
                .isNotEmpty()
                .isNotInMapKeys(orgUnits);
        validation.finish();
        orgUnits.put(orgUnit.getName(), orgUnit);
    }

    @Override
    public void updateOrgUnit(OrgUnit orgUnit) throws AppException {
        Validation validation = new Validation();
        validation.verifyThat(orgUnit.getName()).inField("orgUnit.name")
                .isNotEmpty()
                .isInMapKeys(orgUnits);
        validation.finish();
        orgUnits.put(orgUnit.getName(), orgUnit);
    }

}
