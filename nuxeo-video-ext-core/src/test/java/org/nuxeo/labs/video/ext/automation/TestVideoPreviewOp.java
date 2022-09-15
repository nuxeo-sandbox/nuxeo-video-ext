/*
 * (C) Copyright 2022 Nuxeo (http://nuxeo.com/) and others.
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
 *     Michael Vachette
 */

package org.nuxeo.labs.video.ext.automation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestVideoPreviewOp {

    @Inject
    protected AutomationService automationService;

    @Inject
    VideoExtFeature videoExtFeature;

    @Inject
    CoreSession session;

    @Test
    public void testWithoutTimecode() throws OperationException {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        ctx.setInput(doc);

        doc = (DocumentModel) automationService.run(ctx, VideoPreviewOp.ID, params);

        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>) doc.getPropertyValue("picture:views");
        Assert.assertEquals(2,views.size());
    }

    @Test
    public void testWithTimecode() throws OperationException {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("timecodeInSecond", 2.0d);
        ctx.setInput(doc);

        doc = (DocumentModel) automationService.run(ctx, VideoStoryboardOp.ID, params);

        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>) doc.getPropertyValue("picture:views");
        Assert.assertEquals(2,views.size());
    }

    @Test
    @Deploy("nuxeo-video-ext-core:test-automation-js-contrib.xml")
    public void testWithTimecodesFromAutomationJS() throws OperationException {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        ctx.setInput(doc);

        doc = (DocumentModel) automationService.run(ctx, "javascript.test_video_preview_js", params);

        List<Map<String, Serializable>> views = (List<Map<String, Serializable>>) doc.getPropertyValue("picture:views");
        Assert.assertEquals(2,views.size());
    }

}
