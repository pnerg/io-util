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

import junitextensions.ReflectionAssert;

/**
 * Test the class {@link FileUtil}
 * @author Peter Nerg
 */
public class TestFileUtil extends BaseAssert implements ReflectionAssert {

	private final File testRootDir;
	
	public TestFileUtil() throws IOException {
		testRootDir = createDummyDir();	
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
		assertTrue(delete(new File("/no-such-path")));
	}

	@Test
	public void delete_existingFile() throws IOException {
		File f = createDummyFile();
		assertTrue(delete(f));
		assertPathNotExist(f);
	}

	@Test
	public void delete_emptyDirectory() throws IOException {
		File d = createDummyDir();
		assertTrue(delete(d));
		assertPathNotExist(d);
	}
	
	private void assertPathNotExist(File f) {
		assertFalse("Unexpected file ["+f.getAbsolutePath()+"]", f.exists());
	}

	private File createDummyDir() throws IOException {
		File f = new File("target/TestFileUtil"+System.nanoTime()+"/");
		assertTrue(f.mkdirs());
		assertTrue(f.isDirectory());
		return f;
	}

	private File createDummyFile() throws IOException {
		return createDummyFile(new File("target/"));
	}

	private File createDummyFile(File parent) throws IOException {
		File f = new File(parent, "File"+System.currentTimeMillis()+".dummy");
		assertTrue(f.createNewFile());
		assertTrue(f.isFile());
		return f;
	}
}
