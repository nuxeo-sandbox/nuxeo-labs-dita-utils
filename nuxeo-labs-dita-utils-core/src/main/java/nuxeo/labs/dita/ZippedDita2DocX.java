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
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
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

    private Blob zippedDitaBlob;
    private Path outDirPath;

    public ZippedDita2DocX(Blob inBlob) {

        zippedDitaBlob = inBlob;

        String tmpDir = Environment.getDefault().getTemp().getPath();
        Path tmpDirPath = Paths.get(tmpDir);
        try {
            outDirPath = tmpDirPath != null ? Files.createTempDirectory(tmpDirPath, "dita2docx") : Framework.createTempDirectory(null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public Blob getDocx() {
        Blob docXBlob;
        File ditaMapFile;
        File ditaDocXFile;

        // Extract the zip, get the DITA map file.
        ditaMapFile = unZipDita(zippedDitaBlob);

        // Run the converter on it.
        ditaDocXFile = convertDita2Docx(ditaMapFile);

        // Return that.
        docXBlob = new FileBlob(ditaDocXFile);

        return docXBlob;
    }


    private File convertDita2Docx(File ditaMapFile) {
        Process pr;
        File docXFile = null;
        Runtime rt = Runtime.getRuntime();
        try {
            pr = rt.exec("dita -i " + ditaMapFile.getAbsolutePath() + " -f docx -o " + outDirPath.toAbsolutePath());

            // Ensure that the process completes
            try {
                pr.waitFor();
            } catch (InterruptedException e) {
                // Handle exception that could occur when waiting
                // for a spawned process to terminate
                e.printStackTrace();
            }

            // Then examine the process exit code
            if (pr.exitValue() == 0) {
                // This is totally a hard-coded assumption based on the behavior of `dita`.
                String docXPath = outDirPath.toAbsolutePath() + File.separator + FileUtils.getFileNameNoExt(ditaMapFile.getPath()) + ".docm";
                docXFile = new File(docXPath);
                String newFileName = outDirPath.toAbsolutePath() + File.separator + FileUtils.getFileNameNoExt(ditaMapFile.getPath()) + ".doc";

                if(docXFile.renameTo(new File(newFileName))){
                    docXFile = new File(newFileName);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return docXFile;
    }


    /**
     * Adapted from https://github.com/nuxeo-sandbox/nuxeo-unzip-file
     *
     * @param inBlob
     * @return
     */
    private File unZipDita(Blob inBlob) {
        File result = null;

        try {
            byte[] buffer = new byte[1024];
            int len = 0;

            //create output directory if it doesn't exist
            File folder = new File(outDirPath.toString());

            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis = new ZipInputStream(inBlob.getStream());

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
