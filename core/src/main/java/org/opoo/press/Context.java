/*
 * Copyright 2013 Alex Lin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opoo.press;

import org.opoo.press.source.SourceEntryLoader;
import org.opoo.press.source.SourceManager;
import org.opoo.press.source.SourceParser;

/**
 * The global application context.
 * 
 * @author Alex Lin
 */
public interface Context {
	
	SourceEntryLoader getSourceEntryLoader();
	
	SourceParser getSourceParser();
	
	SiteManager getSiteManager();
	
	SourceManager getSourceManager();
	
	ThemeManager getThemeManager();
	
	<T> T get(String name, Class<T> clazz);
}
