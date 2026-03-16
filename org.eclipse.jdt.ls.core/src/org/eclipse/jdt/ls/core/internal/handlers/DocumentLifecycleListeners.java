/*******************************************************************************
 * Copyright (c) 2026 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.ls.core.internal.handlers;

import org.eclipse.jdt.ls.core.internal.JavaLanguageServerPlugin;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;

/**
 * @author sandreev
 *
 */
public class DocumentLifecycleListeners extends ExtensionRepository<IDocumentLifecycleListener> implements IDocumentLifecycleListener {

	public static final String EXTENSION_POINT_ID = "org.eclipse.jdt.ls.core.documentLifecycleListener";

	public DocumentLifecycleListeners() {
		super(EXTENSION_POINT_ID, IDocumentLifecycleListener.class);
	}

	@Override
	public void didChange(DidChangeTextDocumentParams params) {
		for (IDocumentLifecycleListener l : getImplementers()) {
			try {
				l.didChange(params);
			} catch (Exception e) {
				JavaLanguageServerPlugin.logException("Exception occurred while notifying listener: " + l, e);
			}
		}
	}

	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		for (IDocumentLifecycleListener l : getImplementers()) {
			try {
				l.didClose(params);
			} catch (Exception e) {
				JavaLanguageServerPlugin.logException("Exception occurred while notifying listener: " + l, e);
			}
		}
	}

	@Override
	public void didOpen(DidOpenTextDocumentParams params) {
		for (IDocumentLifecycleListener l : getImplementers()) {
			try {
				l.didOpen(params);
			} catch (Exception e) {
				JavaLanguageServerPlugin.logException("Exception occurred while notifying listener: " + l, e);
			}
		}
	}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
		for (IDocumentLifecycleListener l : getImplementers()) {
			try {
				l.didSave(params);
			} catch (Exception e) {
				JavaLanguageServerPlugin.logException("Exception occurred while notifying listener: " + l, e);
			}
		}
	}
}
