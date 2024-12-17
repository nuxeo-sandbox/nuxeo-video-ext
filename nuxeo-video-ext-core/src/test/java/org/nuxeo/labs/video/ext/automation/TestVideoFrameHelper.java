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

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestVideoFrameHelper {

    @Inject
    protected AutomationService automationService;

    @Inject
    VideoExtFeature videoExtFeature;

    @Inject
    CoreSession session;

    @Test(expected = OperationException.class)
    @Deploy("nuxeo-video-ext-core:test-automation-js-contrib.xml")
    public void testWithBlackFrame() throws OperationException {
        Blob frame = videoExtFeature.getBlackFrameBlob();
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        ctx.setInput(frame);
        automationService.run(ctx, "javascript.test_black_frame_helper", params);
        Assert.assertTrue(true);
    }

    @Test
    @Deploy("nuxeo-video-ext-core:test-automation-js-contrib.xml")
    public void testWithRegularFrame() throws OperationException {
        Blob frame = videoExtFeature.getRegularFrameBlob();
        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        ctx.setInput(frame);
        automationService.run(ctx, "javascript.test_black_frame_helper", params);
        Assert.assertTrue(true);
    }

}
