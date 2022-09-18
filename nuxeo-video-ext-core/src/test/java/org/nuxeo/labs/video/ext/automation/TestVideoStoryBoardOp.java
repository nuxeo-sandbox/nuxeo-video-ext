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

import static org.nuxeo.ecm.platform.video.VideoHelper.DEFAULT_NUMBER_OF_THUMBNAILS;

import java.util.Arrays;
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
import org.nuxeo.labs.video.ext.adapter.StoryboardAdapter;
import org.nuxeo.labs.video.ext.api.Storyboard;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestVideoStoryBoardOp {

    @Inject
    protected AutomationService automationService;

    @Inject
    VideoExtFeature videoExtFeature;

    @Inject
    CoreSession session;

    @Test
    public void testWithoutTimecodes() throws OperationException {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        ctx.setInput(doc);

        doc = (DocumentModel) automationService.run(ctx, VideoStoryboardOp.ID, params);

        Storyboard storyboard = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(DEFAULT_NUMBER_OF_THUMBNAILS, storyboard.size());
    }

    @Test
    public void testWithTimecodes() throws OperationException {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        List<String> timecodes = Arrays.asList("1.000", "2.000", "3.000");
        params.put("timecodeListInSeconds", timecodes);
        ctx.setInput(doc);

        doc = (DocumentModel) automationService.run(ctx, VideoStoryboardOp.ID, params);

        Storyboard storyboard = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(timecodes.size(), storyboard.size());
    }

    @Test
    @Deploy("nuxeo-video-ext-core:test-automation-js-contrib.xml")
    public void testWithTimecodesFromAutomationJS() throws OperationException {
        DocumentModel doc = videoExtFeature.getVideoDocument(session);

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        ctx.setInput(doc);

        doc = (DocumentModel) automationService.run(ctx, "javascript.test_video_storyboard_js", params);

        Storyboard storyboard = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(6, storyboard.size());
    }

}
