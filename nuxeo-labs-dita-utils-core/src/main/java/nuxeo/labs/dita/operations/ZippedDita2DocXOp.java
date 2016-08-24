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

package nuxeo.labs.dita.operations;

import nuxeo.labs.dita.ZippedDita2DocX;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Extract zipped DITA project and convert to DOCX.
 *
 * @since 8.3
 */
@Operation(id = ZippedDita2DocXOp.ID, category = Constants.CAT_BLOB, label = "Zipped DITA to DOCX", description = "Extract zipped DITA project and convert to DOCX.")
public class ZippedDita2DocXOp {

    public static final String ID = "DITA.ZippedDita2DocXOp";

    @Context
    protected CoreSession session;

    @OperationMethod
    public Blob run(Blob inBlob) {
        ZippedDita2DocX zd2d = new ZippedDita2DocX(inBlob);

        return zd2d.getDocx();
    }
}
