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

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;

/**
 * @author Simeon Andreev
 *
 */
public class ContributedTestDocumentLifecycleListener implements IDocumentLifecycleListener {

	static final AtomicInteger change = new AtomicInteger(0);
	static final AtomicInteger close = new AtomicInteger(0);
	static final AtomicInteger open = new AtomicInteger(0);
	static final AtomicInteger save = new AtomicInteger(0);

	static void reset() {
		change.set(0);
		close.set(0);
		open.set(0);
		save.set(0);
	}

	@Override
	public void didChange(DidChangeTextDocumentParams params) {
		change.incrementAndGet();
	}

	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		close.incrementAndGet();
	}

	@Override
	public void didOpen(DidOpenTextDocumentParams params) {
		open.incrementAndGet();
	}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
		save.incrementAndGet();
	}
}
