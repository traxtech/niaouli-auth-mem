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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

  /**
   * All persons, mapped by their system names.
   */
  private final Map<String, Person> persons = new HashMap<String, Person>();
  /**
   * All groups, mapped by their system names.
   */
  private final Map<String, Group> groups = new HashMap<String, Group>();

  /**
   * Persons system names set per group system name.
   */
  private final Map<String, Set<String>> groupsPersons
          = new HashMap<String, Set<String>>();

  /**
   * All organizational units, mapped by their names.
   */
  private final Map<String, OrgUnit> orgUnits
          = new HashMap<String, OrgUnit>();

  /**
   * All passwords, mapped by their person system names.
   */
  private final Map<String, String> passwords = new HashMap<String, String>();

  @Override
  public final void configure(final Map<String, String> props) {
  }

  @Override
  public final boolean checkHealth() {
    return true;
  }

  @Override
  public final boolean checkCredentials(final String sysName,
          final String password) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(sysName).inField("sysName").isNotEmpty();
    validation.finish();
    return passwords.containsKey(sysName)
            && passwords.get(sysName).equals(password);
  }

  @Override
  public final Person loadPerson(final String sysName) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(sysName).inField("sysName")
            .isNotEmpty()
            .isInMapKeys(persons);
    validation.finish();
    return persons.get(sysName);
  }

  @Override
  public final Collection<Person> findPersons() {
    return Collections.unmodifiableCollection(persons.values());
  }

  @Override
  public final boolean canCreateOrUpdatePerson() {
    return true;
  }

  @Override
  public final boolean canUpdatePassword() {
    return true;
  }

  @Override
  public final void createPerson(final Person person) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(person.getSysName()).inField("person.sysName")
            .isNotEmpty()
            .isNotInMapKeys(persons);
    validation.finish();
    persons.put(person.getSysName(), person);
  }

  @Override
  public final void updatePerson(final Person person) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(person.getSysName()).inField("person.sysName")
            .isNotEmpty()
            .isInMapKeys(persons);
    validation.finish();
    persons.put(person.getSysName(), person);
  }

  @Override
  public final void updatePersonPassword(final String sysName,
          final String password) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(sysName).inField("sysName")
            .isNotEmpty()
            .isInMapKeys(persons);
    validation.finish();
    passwords.put(sysName, password);
  }

  @Override
  public final Group loadGroup(final String sysName) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(sysName).inField("sysName")
            .isNotEmpty()
            .isInMapKeys(groups);
    validation.finish();
    return groups.get(sysName);
  }

  @Override
  public final Collection<Group> findGroups() {
    return Collections.unmodifiableCollection(groups.values());
  }

  @Override
  public final Collection<String> findGroupPersons(final String sysName)
          throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(sysName).inField("sysName")
            .isNotBlank()
            .isInMapKeys(groups);
    validation.finish();
    if (groupsPersons.containsKey(sysName)) {
      return Collections.unmodifiableSet(groupsPersons.get(sysName));
    } else {
      return Collections.unmodifiableSet(new HashSet<String>());
    }
  }

  @Override
  public final void attachGroupMember(final String groupSysName,
          final String personSysName) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(groupSysName).inField("groupSysName")
            .isNotBlank()
            .isInMapKeys(groups);
    validation.verifyThat(personSysName).inField("personSysName")
            .isNotBlank()
            .isInMapKeys(persons);
    validation.finish();
    if (groupsPersons.containsKey(groupSysName)) {
      groupsPersons.get(groupSysName).add(personSysName);
    } else {
      Set<String> initMembers = new HashSet<String>();
      initMembers.add(personSysName);
      groupsPersons.put(groupSysName, initMembers);
    }
  }

  @Override
  public final void detachGroupMember(final String groupSysName,
          final String personSysName) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(groupSysName).inField("groupSysName")
            .isNotBlank()
            .isInMapKeys(groups);
    validation.verifyThat(personSysName).inField("personSysName")
            .isNotBlank()
            .isInMapKeys(persons);
    validation.finish();
    if (groupsPersons.containsKey(groupSysName)) {
      groupsPersons.get(groupSysName).remove(personSysName);
    }
  }

  @Override
  public final boolean canCreateOrUpdateGroup() {
    return true;
  }

  @Override
  public final void createGroup(final Group group) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(group.getSysName()).inField("group.sysName")
            .isNotEmpty()
            .isNotInMapKeys(groups);
    validation.finish();
    groups.put(group.getSysName(), group);
  }

  @Override
  public final void updateGroup(final Group group) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(group.getSysName()).inField("group.sysName")
            .isNotEmpty()
            .isInMapKeys(groups);
    validation.finish();
    groups.put(group.getSysName(), group);
  }

  @Override
  public final OrgUnit loadOrgUnit(final String name) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(name).inField("name")
            .isNotEmpty()
            .isInMapKeys(orgUnits);
    validation.finish();
    return orgUnits.get(name);
  }

  @Override
  public final Collection<OrgUnit> findOrgUnits() {
    return Collections.unmodifiableCollection(orgUnits.values());
  }

  @Override
  public final Collection<String> findOrgUnitPersons(final String name)
          throws AppException {
    Set<String> attachedPersons = new HashSet<String>();
    for (Person person : persons.values()) {
      if (person.getOrgUnitName().equals(name)) {
        attachedPersons.add(person.getSysName());
      }
    }
    return Collections.unmodifiableCollection(attachedPersons);
  }

  @Override
  public final boolean canCreateOrUpdateOrgUnit() {
    return true;
  }

  @Override
  public final void createOrgUnit(final OrgUnit orgUnit) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(orgUnit.getName()).inField("orgUnit.name")
            .isNotEmpty()
            .isNotInMapKeys(orgUnits);
    validation.finish();
    orgUnits.put(orgUnit.getName(), orgUnit);
  }

  @Override
  public final void updateOrgUnit(final OrgUnit orgUnit) throws AppException {
    Validation validation = new Validation();
    validation.verifyThat(orgUnit.getName()).inField("orgUnit.name")
            .isNotEmpty()
            .isInMapKeys(orgUnits);
    validation.finish();
    orgUnits.put(orgUnit.getName(), orgUnit);
  }

}
