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

package org.nuxeo.labs.video.ext.listener;

import static org.nuxeo.ecm.platform.video.VideoConstants.TRANSCODED_VIDEOS_PROPERTY;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.listener.VideoChangedListener;
import org.nuxeo.labs.video.ext.service.VideoInfoService;
import org.nuxeo.labs.video.ext.service.VideoStoryBoardService;
import org.nuxeo.runtime.api.Framework;

public class ExtendedVideoChangedListener extends VideoChangedListener {

    private static final Logger log = LogManager.getLogger(ExtendedVideoChangedListener.class);

    protected void resetProperties(DocumentModel doc) throws IOException {
        log.debug("Resetting video info, storyboard, previews and conversions of document {}", doc);
        VideoInfoService videoInfoService = Framework.getService(VideoInfoService.class);
        videoInfoService.clearVideoInfo(doc);
        VideoStoryBoardService videoStoryBoardService = Framework.getService(VideoStoryBoardService.class);
        videoStoryBoardService.clearStoryboard(doc);
        videoStoryBoardService.clearPreviews(doc);
        doc.setPropertyValue(TRANSCODED_VIDEOS_PROPERTY, null);
    }

    @Override
    protected void scheduleAsyncProcessing(DocumentModel doc) {
        VideoDocument videoDocument = doc.getAdapter(VideoDocument.class);
        if (videoDocument.getVideo().getBlob() != null) {
            VideoInfoService videoInfoService = Framework.getService(VideoInfoService.class);
            videoInfoService.scheduleVideoInfoWork(doc);
        } else {
            log.debug("Document doesn't hold a video file, skipping video info, storyboard and conversions of document {}", doc);
        }
    }

}
