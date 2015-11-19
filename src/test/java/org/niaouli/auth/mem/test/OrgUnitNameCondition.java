/*
 * Copyright (c) 2015 Clarly. All rights reserved.
 */
package org.niaouli.auth.mem.test;

import org.assertj.core.api.Condition;
import org.niaouli.auth.OrgUnit;

/**
 *
 * @author Arnaud Rolly <github@niaouli.org>
 */
public class OrgUnitNameCondition extends Condition<OrgUnit> {

    private final String name;

    public OrgUnitNameCondition(String pName) {
        name = pName;
    }

    @Override
    public boolean matches(OrgUnit ou) {
        return (name == null && ou.getName() == null) || name.equals(ou.getName());
    }

}
