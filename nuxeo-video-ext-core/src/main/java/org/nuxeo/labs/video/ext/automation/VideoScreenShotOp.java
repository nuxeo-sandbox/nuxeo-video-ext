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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.labs.video.ext.service.VideoStoryBoardService;

/**
 *
 */
@Operation(id = VideoScreenShotOp.ID, category = "Video", label = "Generate a screenshot", description = "Generate a screenshot")
public class VideoScreenShotOp {

    public static final String ID = "Video.Screenshot";

    private static final Logger log = LogManager.getLogger(VideoScreenShotOp.class);

    @Context
    protected OperationContext ctx;

    @Context
    protected VideoStoryBoardService storyboardService;

    @Param(name = "timecodeInSecond", description = "Timecode in second")
    protected Double timecodeInSecond;

    @OperationMethod
    public Blob run(Blob input) {
        return storyboardService.screenshot(input,timecodeInSecond);
    }

}
