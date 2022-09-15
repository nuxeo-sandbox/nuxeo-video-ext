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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.Constants;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.labs.video.ext.service.VideoStoryBoardService;

/**
 *
 */
@Operation(id = VideoPreviewOp.ID, category = Constants.CAT_BLOB, label = "Generate a preview and thumbnail", description = "Generate a preview and thumbnail image for the input video document")
public class VideoPreviewOp {

    public static final String ID = "Video.Preview";

    private static final Log log = LogFactory.getLog(VideoPreviewOp.class);

    @Context
    protected OperationContext ctx;

    @Context
    protected VideoStoryBoardService storyboardService;

    @Param(name = "timecodeInSecond", description = "Timecode in second", required = false)
    protected Double timecodeInSecond;

    @Param(name = "save", description = "Save modification made to the input document", required = false)
    protected boolean save = false;

    @OperationMethod
    public DocumentModel run(DocumentModel doc) {
        VideoDocument videoDoc = doc.getAdapter(VideoDocument.class);
        if (videoDoc == null) {
            return doc;
        }

        if (timecodeInSecond == null) {
            storyboardService.updatePreviews(doc);
        } else {
            storyboardService.updatePreviews(doc,timecodeInSecond.doubleValue());
        }

        if (save) {
            doc = ctx.getCoreSession().saveDocument(doc);
        }

        return doc;
    }

}