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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(FeaturesRunner.class)
@Features(AutomationFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy("nuxeo.labs.dita.utils.core.nuxeo-labs-dita-utils-core")
public class TestZippedDita2DocX {

    protected static final String THE_ZIP = "files/dita_test.zip";
    protected File zipFile;
    protected FileBlob zipFileBlob;

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    @Before
    public void setup() throws IOException {

        assertNotNull(session);
        assertNotNull(automationService);

        zipFile = FileUtils.getResourceFileFromContext(THE_ZIP);
        zipFileBlob = new FileBlob(zipFile);

    }

    @Test
    public void testDitaZipExtraction() throws OperationException {
        final String ditaMapFileName = "dita_test.ditamap";

        ZippedDita2DocX pe = new ZippedDita2DocX(zipFileBlob);

        Blob result = pe.getDocx();

        assertEquals(result.getFilename(), ditaMapFileName);
    }


}
