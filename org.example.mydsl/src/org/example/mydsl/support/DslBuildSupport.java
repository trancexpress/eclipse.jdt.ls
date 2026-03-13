package org.example.mydsl.support;

import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ls.core.internal.JDTUtils;
import org.eclipse.jdt.ls.core.internal.JavaClientConnection;
import org.eclipse.jdt.ls.core.internal.JavaLanguageServerPlugin;
import org.eclipse.jdt.ls.core.internal.ResourceUtils;
import org.eclipse.jdt.ls.core.internal.managers.IBuildSupport;
import org.eclipse.jdt.ls.core.internal.managers.ProjectsManager.CHANGE_TYPE;
import org.eclipse.lsp4j.PublishDiagnosticsParams;

@SuppressWarnings("restriction")
public class DslBuildSupport implements IBuildSupport {

	@Override
	public boolean applies(IProject project) {
		return true;
	}

	@Override
	public boolean isBuildFile(IResource r) {
		return r != null && Utilities.DSL_EXTENSION.equals(r.getFileExtension());
	}

	@Override
	public void refresh(IResource resource, CHANGE_TYPE changeType, IProgressMonitor monitor) throws CoreException {
	}
	
	@Override
	public void compile(IResource resource, IProgressMonitor monitor) {
		cleanDiagnostics(resource);
	}

	public static void cleanDiagnostics(IResource resource) {
		String uri = null;
		if (resource != null) {
			uri = JDTUtils.getFileURI(resource);
		}
		if (uri != null) {
			JavaLanguageServerPlugin.logInfo("Cleaning diagnostics for: " + uri);
			connection().publishDiagnostics(cleanDiagnosticsParams(uri));
		}
	}

	private static JavaClientConnection connection() {
		return JavaLanguageServerPlugin.getInstance().getClientConnection();
	}

	private static PublishDiagnosticsParams cleanDiagnosticsParams(String uri) {
		return new PublishDiagnosticsParams(ResourceUtils.toClientUri(uri), Collections.emptyList());
	}
}
