/*******************************************************************************
 * Copyright (c) 2026 Simeon Andreev and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.ls.core.internal.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ls.core.internal.ExtensionRegistryUtil;
import org.eclipse.jdt.ls.core.internal.JavaLanguageServerPlugin;
import org.eclipse.jdt.ls.core.internal.managers.AbstractProjectsManagerBasedTest;
import org.eclipse.jdt.ls.core.internal.preferences.ClientPreferences;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DocumentLifecycleListenerTest extends AbstractProjectsManagerBasedTest {

	private static final String EXTENSION_XML = """
			<plugin>
			   <extension
			      id="testDocumentLifecycleListener"
			      point="org.eclipse.jdt.ls.core.documentLifecycleListener">
			      <documentLifecycleListener
			          id="testListener"
			          class="org.eclipse.jdt.ls.core.internal.handlers.ContributedTestDocumentLifecycleListener" />
			   </extension>
			</plugin>
			""";

	private static final String INITIAL_CONTENTS = "test contents";

	private DocumentLifeCycleHandler lifeCycleHandler;

	private IJavaProject javaProject;

	private IFile file;

	@Mock
	private ClientPreferences clientPreferences;

	@BeforeEach
	public void setup() throws Exception {
		JDTLanguageServer server = new JDTLanguageServer(projectsManager, preferenceManager);
		server.connectClient(client);
		lifeCycleHandler = new DocumentLifeCycleHandler(server.getClientConnection(), preferenceManager, projectsManager, false);
		JavaLanguageServerPlugin.getNonProjectDiagnosticsState().setGlobalErrorLevel(true);
		JavaLanguageServerPlugin.getInstance().setProtocol(server);

		ExtensionRegistryUtil.addExtension(EXTENSION_XML, "testContributedListener");
		ContributedTestDocumentLifecycleListener.reset();

		javaProject = newEmptyProject();
		IProject project = javaProject.getProject();
		file = project.getFile("test.txt");
		file.create(INITIAL_CONTENTS.getBytes(), IResource.FORCE, monitor);
	}

	@AfterEach
	public void tearDown() throws Exception {
		ExtensionRegistryUtil.removeExtension("testDocumentLifecycleListener");
		file.delete(true, monitor);
	}

	@Test
	public void testLifecycleListener() throws Exception {
		String uri = file.getLocationURI().toString();

		openDocument(uri, INITIAL_CONTENTS, 1);
		assertEquals(0, ContributedTestDocumentLifecycleListener.change.get(), "unexpected change notifications");
		assertEquals(0, ContributedTestDocumentLifecycleListener.close.get(), "unexpected close notifications");
		assertEquals(1, ContributedTestDocumentLifecycleListener.open.get(), "unexpected open notifications");
		assertEquals(0, ContributedTestDocumentLifecycleListener.save.get(), "unexpected save notifications");

		String contents = "changed contents";
		changeDocument(uri, contents, 2);
		assertEquals(1, ContributedTestDocumentLifecycleListener.change.get(), "unexpected change notifications");
		assertEquals(0, ContributedTestDocumentLifecycleListener.close.get(), "unexpected close notifications");
		assertEquals(1, ContributedTestDocumentLifecycleListener.open.get(), "unexpected open notifications");
		assertEquals(0, ContributedTestDocumentLifecycleListener.save.get(), "unexpected save notifications");

		saveDocument(uri, contents);
		assertEquals(1, ContributedTestDocumentLifecycleListener.change.get(), "unexpected change notifications");
		assertEquals(0, ContributedTestDocumentLifecycleListener.close.get(), "unexpected close notifications");
		assertEquals(1, ContributedTestDocumentLifecycleListener.open.get(), "unexpected open notifications");
		assertEquals(1, ContributedTestDocumentLifecycleListener.save.get(), "unexpected save notifications");

		closeDocument(uri);
		assertEquals(1, ContributedTestDocumentLifecycleListener.change.get(), "unexpected change notifications");
		assertEquals(1, ContributedTestDocumentLifecycleListener.close.get(), "unexpected close notifications");
		assertEquals(1, ContributedTestDocumentLifecycleListener.open.get(), "unexpected open notifications");
		assertEquals(1, ContributedTestDocumentLifecycleListener.save.get(), "unexpected save notifications");
	}

	private void openDocument(String uri, String content, int version) {
		DidOpenTextDocumentParams openParms = new DidOpenTextDocumentParams();
		TextDocumentItem textDocument = new TextDocumentItem();
		textDocument.setLanguageId("java");
		textDocument.setText(content);
		textDocument.setUri(uri);
		textDocument.setVersion(version);
		openParms.setTextDocument(textDocument);
		lifeCycleHandler.didOpen(openParms);
	}

	private void changeDocument(String uri, String content, int version) throws JavaModelException {
		DidChangeTextDocumentParams changeParms = new DidChangeTextDocumentParams();
		VersionedTextDocumentIdentifier textDocument = new VersionedTextDocumentIdentifier();
		textDocument.setUri(uri);
		textDocument.setVersion(version);
		changeParms.setTextDocument(textDocument);
		TextDocumentContentChangeEvent event = new TextDocumentContentChangeEvent();
		event.setText(content);
		List<TextDocumentContentChangeEvent> contentChanges = new ArrayList<>();
		contentChanges.add(event);
		changeParms.setContentChanges(contentChanges);
		lifeCycleHandler.didChange(changeParms);
	}

	private void saveDocument(String uri, String content) throws Exception {
		DidSaveTextDocumentParams saveParms = new DidSaveTextDocumentParams();
		TextDocumentIdentifier textDocument = new TextDocumentIdentifier();
		textDocument.setUri(uri);
		saveParms.setTextDocument(textDocument);
		saveParms.setText(content);
		lifeCycleHandler.didSave(saveParms);
		DocumentLifeCycleHandler.handleFileRenameForTypeDeclaration(uri);
		waitForBackgroundJobs();
	}

	private void closeDocument(String uri) {
		DidCloseTextDocumentParams closeParms = new DidCloseTextDocumentParams();
		TextDocumentIdentifier textDocument = new TextDocumentIdentifier();
		textDocument.setUri(uri);
		closeParms.setTextDocument(textDocument);
		lifeCycleHandler.didClose(closeParms);
	}
}
