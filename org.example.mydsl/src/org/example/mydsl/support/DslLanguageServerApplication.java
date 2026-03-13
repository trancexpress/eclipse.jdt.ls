package org.example.mydsl.support;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.ls.core.internal.LanguageServerApplication;

@SuppressWarnings("restriction")
public class DslLanguageServerApplication extends LanguageServerApplication {

	private static final ResourceListener LISTENER = new ResourceListener();
	@Override
	public Object start(IApplicationContext context) throws Exception {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(LISTENER);
		return super.start(context);
	}
	
	@Override
	public void stop() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(LISTENER);
		super.stop();
	}
}
