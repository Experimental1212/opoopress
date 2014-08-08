/*
 * Copyright 2014 Alex Lin.
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
package org.opoo.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Alex Lin
 *
 */
public abstract class PathUtils {
	public static enum Strategy{
		NONE, CREATE_IF_NOT_EXISTS, THROW_EXCEPTION_IF_NOT_EXISTS, CREATE_ALWAYS;
	}
	
	public static void checkDir(File dir, Strategy p){
		if(!dir.exists()){
			if(p == Strategy.CREATE_IF_NOT_EXISTS){
				dir.mkdirs();
			}else if(p == Strategy.THROW_EXCEPTION_IF_NOT_EXISTS){
				throw new IllegalArgumentException("Directory not exits: " + dir);
			}
		}else{
			if(!dir.isDirectory()){
				throw new IllegalArgumentException("Path exits but not a directory: " + dir);
			}
		}
		//Create new directory whatever
		if(Strategy.CREATE_ALWAYS == p){
			if(dir.exists()){
				FileUtils.deleteQuietly(dir);
			}
			dir.mkdirs();
		}
	}
	
	public static void checkFile(File file, Strategy p){
		if(!file.exists()){
			if(Strategy.CREATE_IF_NOT_EXISTS == p){
				file.getParentFile().mkdirs();
			}else if(Strategy.THROW_EXCEPTION_IF_NOT_EXISTS == p){
				throw new IllegalArgumentException("File not exits: " + file);
			}
		}else{
			if(!file.isFile()){
				throw new IllegalArgumentException("Path exits but not a file: " + file);
			}
		}
		
		//Create new directory whatever
		if(Strategy.CREATE_ALWAYS == p){
			if(file.exists()){
				FileUtils.deleteQuietly(file);
			}
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
		}
	}
	
	public static File canonical(File file){
		try {
			return file.getCanonicalFile();
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public static boolean isValidDirectory(File dir){
		return dir != null && dir.exists() && dir.isDirectory() && dir.canRead();
	}
	
	public static File appendBaseIfNotAbsolute(File basedir, String name){
		File file = null;

		//maybe absolute path
		if(FilenameUtils.separatorsToUnix(name).indexOf('/') != -1){
			File tmp = new File(name);
			if(tmp.isAbsolute()){
				file = tmp;
			}
		}
		
		if(file == null){
			file = new File(basedir, name);
		}
		
		return PathUtils.canonical(file);
	}
	
	public static List<File> listFiles(File dir, FileFilter filter, boolean recursive){
		List<File> list = new ArrayList<File>();
		listFilesInternal(list, dir, filter, recursive);
		return list;
	}

	private static void listFilesInternal(List<File> list, File dir, FileFilter filter, boolean recursive) {
		File[] files = dir.listFiles(filter);
		for(File f: files){
			if(f.isFile()){
				list.add(f);
			}else if(recursive && f.isDirectory()){
				listFilesInternal(list, f, filter, recursive);
			}
		}
	}
	
	public static List<File> listFiles(File dir, FilenameFilter filter, boolean recursive){
		List<File> list = new ArrayList<File>();
		listFilesInternal(list, dir, filter, recursive);
		return list;
	}

	private static void listFilesInternal(List<File> list, File dir, FilenameFilter filter, boolean recursive) {
		File[] files = dir.listFiles(filter);
		for(File f: files){
			if(f.isFile()){
				list.add(f);
			}else if(recursive && f.isDirectory()){
				listFilesInternal(list, f, filter, recursive);
			}
		}
	}
}
