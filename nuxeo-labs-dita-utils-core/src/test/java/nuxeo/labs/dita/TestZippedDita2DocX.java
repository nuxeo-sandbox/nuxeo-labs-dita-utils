package nuxeo.labs.dita;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import nuxeo.labs.dita.operations.ZippedDita2DocXOp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.DefaultRepositoryInit;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(AutomationFeature.class)
@RepositoryConfig(init = DefaultRepositoryInit.class, cleanup = Granularity.METHOD)
@Deploy("nuxeo.labs.dita.utils.core.nuxeo-labs-dita-utils-core")
public class TestZippedDita2DocX {

    @Inject
    protected CoreSession session;

    @Inject
    protected AutomationService automationService;

    @Test
    public void shouldCallTheOperation() throws OperationException {
        OperationContext ctx = new OperationContext(session);
        DocumentModel doc = (DocumentModel) automationService.run(ctx, ZippedDita2DocXOp.ID);
        assertEquals("/", doc.getPathAsString());
    }

    @Test
    public void shouldCallWithParameters() throws OperationException {
        final String path = "/default-domain";
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("path", path);
        DocumentModel doc = (DocumentModel) automationService.run(ctx, ZippedDita2DocXOp.ID, params);
        assertEquals(path, doc.getPathAsString());
    }
}
