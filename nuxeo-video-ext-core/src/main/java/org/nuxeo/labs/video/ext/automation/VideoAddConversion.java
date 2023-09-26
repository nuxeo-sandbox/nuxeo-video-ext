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

import static org.nuxeo.ecm.platform.video.VideoConstants.TRANSCODED_VIDEOS_PROPERTY;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.video.TranscodedVideo;
import org.nuxeo.ecm.platform.video.VideoHelper;
import org.nuxeo.ecm.platform.video.VideoInfo;

/**
 *
 */
@Operation(id = VideoAddConversion.ID, category = "Video", label = "Add a stored conversion", description = "Add a stored conversion")
public class VideoAddConversion {

    public static final String ID = "Video.AddConversion";

    private static final Logger log = LogManager.getLogger(VideoAddConversion.class);

    @Context
    protected OperationContext ctx;

    @Context
    CoreSession session;

    @Param(name = "name", required = true)
    String name;

    @Param(name = "videoBlob", required = true)
    Blob videoBlob;

    @Param(name = "save", required = false)
    boolean save = false;

    @OperationMethod
    public DocumentModel run(DocumentModel doc) {

        @SuppressWarnings("unchecked")
        var transcodedVideos = (List<Map<String, Serializable>>) doc.getPropertyValue(TRANSCODED_VIDEOS_PROPERTY);

        transcodedVideos.removeIf(tv -> name.equals(tv.get("name")));

        VideoInfo videoInfo = VideoHelper.getVideoInfo(videoBlob);

        TranscodedVideo tv = TranscodedVideo.fromBlobAndInfo(name,videoBlob,videoInfo);

        transcodedVideos.add(tv.toMap());

        doc.setPropertyValue(TRANSCODED_VIDEOS_PROPERTY, (Serializable) transcodedVideos);

        if (save) {
            session.saveDocument(doc);
        }

        return doc;
    }

}
