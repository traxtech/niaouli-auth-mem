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
import org.junit.Before;
import org.junit.Test;
import org.niaouli.auth.OrgUnit;
import org.niaouli.auth.OrgUnitBuilder;
import org.niaouli.auth.mem.MemAuthSystem;
import org.niaouli.exception.AppException;

/**
 *
 * @author Arnaud Rolly <github@niaouli.org>
 */
public class OrgUnitTest {

    private static final String MARKETING = "Marketing";

    private MemAuthSystem authSystem;

    @Before
    public void before() {
        authSystem = new MemAuthSystem();
    }

    @Test
    public void testNominal() throws AppException {
        assertThat(authSystem.canCreateOrUpdateOrgUnit()).isTrue();
        createOrgUnit(MARKETING);
        OrgUnit ou = authSystem.loadOrgUnit(MARKETING);
        assertThat(ou).isNotNull();
        assertThat(authSystem.findOrgUnits()).are(new OrgUnitNameCondition(MARKETING));
    }

    private void createOrgUnit(String name) throws AppException {
        OrgUnitBuilder builder = new OrgUnitBuilder();
        builder.setName(name);
        authSystem.createOrgUnit(builder.build());
    }
}
