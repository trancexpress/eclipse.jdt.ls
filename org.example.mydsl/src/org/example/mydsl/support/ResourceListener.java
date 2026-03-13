package org.example.mydsl.support;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.ls.core.internal.JavaLanguageServerPlugin;

@SuppressWarnings("restriction")
public class ResourceListener implements IResourceChangeListener, IResourceDeltaVisitor  {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			IResourceDelta delta = event.getDelta();
			delta.accept(this);
		} catch (CoreException e) {
			JavaLanguageServerPlugin.logException("Exception occurred while checking resource changes", e);
		}

	}

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
		if (delta.getFlags() == IResourceDelta.MARKERS) {
			return false;
		}
		IResource resource = delta.getResource();
		if (resource == null) {
			return false;
		}
		if (resource.getType() == IResource.FOLDER || resource.getType() == IResource.ROOT || resource.getType() == IResource.PROJECT) {
			return true;
		}
		JavaLanguageServerPlugin.logInfo("Resource changed: " + resource + ", " + (resource != null ? resource.getName() :""));
		if (resource != null && resource.getName().endsWith(Utilities.DSL_EXTENSION)) {
			new WorkspaceJob("Dummy markers") {
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					if (resource.getType() == IResource.FILE) {
						IFile file = (IFile) resource;
						String string = file.readString();
						resource.deleteMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, false, IResource.DEPTH_ONE);
						DslBuildSupport.cleanDiagnostics(resource);
						if (string.contains("bad_string")) {
							Map<String, String> attributes = new HashMap<>();
							attributes.put(IMarker.MESSAGE, "test message");
							resource.createMarker(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, attributes);
						}
					}
					return Status.OK_STATUS;
				}
			}.schedule();

		}
		return false;
	}
}
