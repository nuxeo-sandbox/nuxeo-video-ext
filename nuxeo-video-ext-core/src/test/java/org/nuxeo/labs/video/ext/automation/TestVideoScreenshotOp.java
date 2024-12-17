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
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestVideoScreenshotOp {

    @Inject
    protected AutomationService automationService;

    @Inject
    VideoExtFeature videoExtFeature;

    @Inject
    CoreSession session;


    @Test
    public void testWithIntegerTimecode() throws OperationException {
        Blob blob = videoExtFeature.getVideoBlob();

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("timecodeInSecond", 2);
        ctx.setInput(blob);

        Blob screenshot = (Blob) automationService.run(ctx, VideoScreenShotOp.ID, params);

        Assert.assertNotNull(screenshot);
    }

    @Test
    public void testWithDoubleTimecode() throws OperationException {
        Blob blob = videoExtFeature.getVideoBlob();

        OperationContext ctx = new OperationContext(session);
        Map<String, Object> params = new HashMap<>();
        params.put("timecodeInSecond", 2.0d);
        ctx.setInput(blob);

        Blob screenshot = (Blob) automationService.run(ctx, VideoScreenShotOp.ID, params);

        Assert.assertNotNull(screenshot);
    }

}
