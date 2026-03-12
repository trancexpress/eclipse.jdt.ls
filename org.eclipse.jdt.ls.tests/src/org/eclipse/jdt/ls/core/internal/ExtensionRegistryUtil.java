/*******************************************************************************
 * Copyright (c) 2026 Simeon Andreev and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.ls.core.internal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;

import org.eclipse.core.internal.registry.ExtensionRegistry;
import org.eclipse.core.runtime.ContributorFactoryOSGi;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * @author sandreev
 *
 */
public class ExtensionRegistryUtil {

	private static final Object TOKEN = ((ExtensionRegistry) Platform.getExtensionRegistry()).getTemporaryUserToken();

	public static void addExtension(String xml, String name) {
		ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		Bundle bundle = FrameworkUtil.getBundle(ExtensionRegistryUtil.class);
		IContributor contributor = ContributorFactoryOSGi.createContributor(bundle);
		boolean result = registry.addContribution(is, contributor, false, name, null, TOKEN);
		assertTrue(result, "Failed to add extension with name: " + name);
	}

	public static void removeExtension(String name) {
		Bundle bundle = FrameworkUtil.getBundle(ExtensionRegistryUtil.class);
		String bundleName = bundle.getSymbolicName();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtension extension = registry.getExtension(bundleName + "." + name);
		assertNotNull(extension, "Failed to find extension");
		boolean result = registry.removeExtension(extension, TOKEN);
		assertTrue(result, "Failed to remove extension: " + name);
	}
}
