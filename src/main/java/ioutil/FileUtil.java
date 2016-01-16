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

import static javascalautils.OptionCompanion.Option;
import static javascalautils.TryCompanion.Try;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.stream.Stream;

import javascalautils.Try;


/**
 * Utilities for file management.
 * 
 * @author Peter Nerg
 * @since 1.0
 */
public final class FileUtil {

	/**
	 * Inhibitive constructor.
	 */
	private FileUtil() {
	}

	/**
	 * Deletes the provided file path. <br>
	 * In case the path denotes a directory the method will recursively go through the provided directory and delete all files/directories it finds. <br>
	 * A non-existing path is counted as successful as it in fact no longer exists after this operation.
	 * 
	 * @param path
	 *            The path to delete
	 * @return <code>true</code> only if the path and all its possible child paths are deleted
	 * @since 1.0
	 */
	public static boolean delete(File path) {
		// if path is a directory, list and delete each found child first
		// Map the found file array to a stream and then delete each file entry in the stream
		Option(path.listFiles()).map(Stream::of).forEach(s -> s.forEach(FileUtil::delete));

		// don't try to delete a non-existing file, we deem the result as true as the file does not exist
		return path.exists() ? path.delete() : true;
	}

	/**
	 * Creates the requested directory. <br>
	 * The operation will fail if the directory already exists or could not be created.
	 * @param parent
	 *            The parent directory
	 * @param name
	 *            The name of the sub-directory
	 * @return Either Success(File) or a Failure with the exception
	 * @since 1.0
	 */
	public static Try<File> mkdir(File parent, String name) {
		return Try(() -> {
			File dir = new File(parent, name);

			// bail if the directory already exists
			if (dir.exists()) {
				throw new FileAlreadyExistsException("Directory already exists [" + dir.getAbsolutePath() + "]");
			}

			// bail if we fail to create the directory
			if (!dir.mkdirs()) {
				throw new IOException("Failed to create dir [" + dir.getAbsolutePath() + "]");
			}

			return dir;
		});
	}
}
