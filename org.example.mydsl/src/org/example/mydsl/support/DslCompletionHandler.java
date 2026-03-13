package org.example.mydsl.support;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ls.core.internal.JavaLanguageServerPlugin;
import org.eclipse.jdt.ls.core.internal.handlers.ICompletionHandler;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;

@SuppressWarnings("restriction")
public class DslCompletionHandler implements ICompletionHandler {

	@Override
	public CompletionList completion(CompletionParams params, IProgressMonitor monitor) {
		List<CompletionItem> items = new ArrayList<>();
		String uri = params.getTextDocument().getUri();
		JavaLanguageServerPlugin.logInfo("DSL completion, uri: " + uri);
		if (uri.endsWith("." + Utilities.DSL_EXTENSION)) {
			CompletionItem item = new CompletionItem();
			item.setLabel("test suggestion");
			item.setDetail("test suggestion detail");
			item.setKind(CompletionItemKind.Text);
			item.setInsertText("tst1");
			items.add(item);
		}
		return new CompletionList(items);
	}

}
