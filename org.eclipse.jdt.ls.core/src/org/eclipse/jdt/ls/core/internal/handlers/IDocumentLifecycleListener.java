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
package org.eclipse.jdt.ls.core.internal.handlers;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;

/**
 * @author Simeon Andreev
 *
 */
public interface IDocumentLifecycleListener {

	default void didClose(DidCloseTextDocumentParams params) {

	}

	default void didOpen(DidOpenTextDocumentParams params) {

	}

	default void didChange(DidChangeTextDocumentParams params) {

	}

	default void didSave(DidSaveTextDocumentParams params) {

	}
}
