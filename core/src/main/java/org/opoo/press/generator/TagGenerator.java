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
package org.opoo.press.generator;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opoo.press.Generator;
import org.opoo.press.Page;
import org.opoo.press.Pager;
import org.opoo.press.Post;
import org.opoo.press.Renderer;
import org.opoo.press.Site;
import org.opoo.press.Tag;
import org.opoo.press.impl.AbstractConvertible;
import org.opoo.press.source.Source;
import org.opoo.util.URLUtils;

/**
 * @author Alex Lin
 *
 */
public class TagGenerator implements Generator {

	/* (non-Javadoc)
	 * @see org.opoo.press.Ordered#getOrder()
	 */
	@Override
	public int getOrder() {
		return 300;
	}

	/* (non-Javadoc)
	 * @see org.opoo.press.Generator#generate(org.opoo.press.Site)
	 */
	@Override
	public void generate(Site site) {
		List<Tag> tags = site.getTags();
		String tagTitlePrefix = site.getConfig().get("tag_title_prefix", "");
		
		for(Tag tag : tags){
			List<Post> posts = tag.getPosts();
			if(posts.isEmpty()){
				continue;
			}
			Collections.sort(posts);
			Collections.reverse(posts);

			TagPage page = new TagPage(site);
			page.setTitle(tagTitlePrefix + tag.getName());
			page.setUrl(tag.getUrl());
			page.setPosts(posts);
			
			site.getPages().add(page);
		}
	}
	
	
	public static class TagPage extends AbstractConvertible implements Page{
		public static final String TEMPLATE = "tag.ftl";
		private String url;
		private Renderer renderer;
		private Site site;
		private String content;// = "<#include \"category_index.ftl\">";
		private String title;
		private List<Post> posts;
		
		public TagPage(Site site) {
			super();
			this.site = site;
			this.renderer = site.getRenderer();
		}

		@Override
		public void render(Map<String, Object> rootMap) {
			rootMap = new HashMap<String,Object>(rootMap);
			mergeRootMap(rootMap);

			String output = getRenderer().render(TEMPLATE, rootMap);
			setContent(output);
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
		
		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getSource()
		 */
		@Override
		public Source getSource() {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getContent()
		 */
		@Override
		public String getContent() {
			return content;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getPath()
		 */
		@Override
		public String getPath() {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getLayout()
		 */
		@Override
		public String getLayout() {
			return "nil";
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getPermalink()
		 */
		@Override
		public String getPermalink() {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getDate()
		 */
		@Override
		public Date getDate() {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getUpdated()
		 */
		@Override
		public Date getUpdated() {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getDateFormatted()
		 */
		@Override
		public String getDateFormatted() {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#getUpdatedFormatted()
		 */
		@Override
		public String getUpdatedFormatted() {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Page#getPager()
		 */
		@Override
		public Pager getPager() {
			return null;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Page#setPager(org.opoo.press.Pager)
		 */
		@Override
		public void setPager(Pager pager) {
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.impl.AbstractConvertible#setContent(java.lang.String)
		 */
		@Override
		public void setContent(String content) {
			this.content = content;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.impl.AbstractConvertible#getOutputFileExtension()
		 */
		@Override
		public String getOutputFileExtension() {
			return ".html";
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.impl.AbstractConvertible#getRenderer()
		 */
		@Override
		protected Renderer getRenderer() {
			return renderer;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.impl.AbstractConvertible#convert()
		 */
		@Override
		public void convert() {
			//do nothing
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.impl.AbstractConvertible#mergeRootMap(java.util.Map)
		 */
		@Override
		protected void mergeRootMap(Map<String, Object> rootMap) {
			rootMap.put("canonical", site.buildCanonical(getUrl()));
			rootMap.put("page", this);
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<Post> getPosts() {
			return posts;
		}

		public void setPosts(List<Post> posts) {
			this.posts = posts;
		}
		
		public boolean isFooter(){
			return false;
		}
		
		public boolean isSidebar(){
			return true;
		}
		
		public Object get(String string){
			return null;
		}

		@Override
		public File getOutputFile(File dest) {
			String url = getUrl() + "index.html";
			url = URLUtils.decodeURL(url);
			File target = new File(dest, url);
			return target;
		}
		
		public boolean isComments(){
			return false;
		}

		/* (non-Javadoc)
		 * @see org.opoo.press.Base#set(java.lang.String, java.lang.Object)
		 */
		@Override
		public void set(String name, Object value) {
		}
	}
}
