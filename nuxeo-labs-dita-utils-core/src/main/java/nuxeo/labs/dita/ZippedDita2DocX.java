/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Josh Fletcher
 */

package nuxeo.labs.dita;

import org.nuxeo.common.Environment;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.blob.binary.BinaryBlob;
import org.nuxeo.runtime.api.Framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extract zipped DITA project and convert to DOCX.
 *
 * @since 8.3
 */
public class ZippedDita2DocX {

    final int BUFFER = 2048;

    private Blob zippedDitaBlob;

    public ZippedDita2DocX(Blob inBlob) {
        zippedDitaBlob = inBlob;
    }

    public Blob getDocx() {
        Blob docXBlob ;
        File ditaMapFile ;

        // Extract the zip to Nuxeo temp folder.
        ditaMapFile = unZipDita(zippedDitaBlob);

        // Run the converter on it.

        // Locate the output file.

        // Return that.
         docXBlob = new FileBlob(ditaMapFile);

        return docXBlob;
    }


    private File unZipDita(Blob inBlob) {
        String tmpDir = Environment.getDefault().getTemp().getPath();
        Path tmpDirPath = tmpDir != null ? Paths.get(tmpDir) : null;
        Path outDirPath;
        File result = null;

        try {
            outDirPath = tmpDirPath != null ? Files.createTempDirectory(tmpDirPath, "dita2docx") : Framework.createTempDirectory(null);
            byte[] buffer = new byte[1024];
            int len = 0;

            //create output directory if it doesn't exist
            File folder = new File(outDirPath.toString());

            if (!folder.exists()) {
                folder.mkdir();
            }

            //copy the input file on temp folder
            BinaryBlob zipBlob = (BinaryBlob) inBlob;

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(zipBlob.getStream());

            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();

                if (fileName.startsWith("__MACOSX/") || fileName.startsWith(".") || fileName.endsWith(".DS_Store")) {
                    ze = zis.getNextEntry();
                    continue;
                }

                String path = fileName.lastIndexOf("/") == -1 ? "" : fileName.substring(0, fileName.lastIndexOf("/"));

                if (ze.isDirectory()) {

                    path = path.indexOf("/") == -1 ? "" : path;

                    File newFile = new File(outDirPath.toString() + File.separator + fileName);
                    newFile.mkdirs();

                    ze = zis.getNextEntry();
                    continue;
                }

                File newFile = new File(outDirPath.toString() + File.separator + fileName);
                FileOutputStream fos = new FileOutputStream(newFile);

                // Need to return the ditamap file.
                if (fileName.endsWith("ditamap")) {
                    result = newFile;
                }

                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
