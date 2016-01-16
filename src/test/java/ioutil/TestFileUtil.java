/**
 *  Copyright 2016 Peter Nerg
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package ioutil;

import static ioutil.FileUtil.delete;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;

import javascalautils.Try;
import junitextensions.ReflectionAssert;
import junitextensions.TryAssert;

/**
 * Test the class {@link FileUtil}
 * @author Peter Nerg
 */
public class TestFileUtil extends BaseAssert implements ReflectionAssert, TryAssert {

	private final File testRootDir;
	
	public TestFileUtil() throws IOException {
		testRootDir = createEmptyDir();	
	}
	
	@After
	public void after() {
		delete(testRootDir);
		assertPathNotExist(testRootDir);
	}
	
	@Test
	public void testPrivateConstructor() {
		assertPrivateConstructor(FileUtil.class);
	}
	
	@Test
	public void delete_nonExistingPath() {
		deleteAndAssertPath(new File(testRootDir,"no-such-path"));
	}

	@Test
	public void delete_existingFile() throws IOException {
		deleteAndAssertPath(createDummyFile());
	}

	@Test
	public void delete_emptyDirectory() throws IOException {
		deleteAndAssertPath(createEmptyDir());
	}

	@Test
	public void delete_nonEmptyDirectory() throws IOException {
		//create a dir and add two file to it
		File dir = createEmptyDir();
		createDummyFile(dir);
		createDummyFile(dir);
		
		deleteAndAssertPath(dir);
	}

	@Test
	public void mkdir_success() throws Throwable {
		Try<File> newDir = FileUtil.mkdir(testRootDir, "mkdir");
		assertSuccess(newDir);
		assertTrue(newDir.get().isDirectory());
	}	
	
	@Test
	public void delete_directoryWithDirectories() throws IOException {
		//create a dir and add two file to it
		File dir = createEmptyDir();
		createDummyFile(dir);
		createDummyFile(dir);
		
		deleteAndAssertPath(testRootDir);
	}
	
	private static void deleteAndAssertPath(File path) {
		assertTrue(delete(path));
		assertPathNotExist(path);
	}
	
	private static void assertPathNotExist(File f) {
		assertFalse("Unexpected file ["+f.getAbsolutePath()+"]", f.exists());
	}

	private File createEmptyDir() throws IOException {
		File f = new File(testRootDir, "Dir"+System.nanoTime()+"/");
		assertTrue(f.mkdirs());
		assertTrue(f.isDirectory());
		return f;
	}

	private File createDummyFile() throws IOException {
		return createDummyFile(testRootDir);
	}

	private File createDummyFile(File parent) throws IOException {
		File f = new File(parent, "File"+System.nanoTime()+".dummy");
		assertTrue(f.createNewFile());
		assertTrue(f.isFile());
		return f;
	}
}
