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

package org.nuxeo.labs.video.ext.utils;

import static org.nuxeo.ecm.platform.video.VideoInfo.DURATION;
import static org.nuxeo.ecm.platform.video.VideoInfo.FORMAT;
import static org.nuxeo.ecm.platform.video.VideoInfo.FRAME_RATE;
import static org.nuxeo.ecm.platform.video.VideoInfo.HEIGHT;
import static org.nuxeo.ecm.platform.video.VideoInfo.STREAMS;
import static org.nuxeo.ecm.platform.video.VideoInfo.WIDTH;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.automation.test.AutomationFeature;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.FileBlob;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.RunnerFeature;

@Features({ AutomationFeature.class })
@Deploy({ "nuxeo-video-ext-core", "org.nuxeo.ecm.platform.picture.core", "org.nuxeo.ecm.platform.tag",
        "org.nuxeo.ecm.platform.video", "nuxeo-video-ext-core:disable-video-changed-listener-contrib.xml" })
public class VideoExtFeature implements RunnerFeature {

    public Blob getVideoBlob() {
        return new FileBlob(FileUtils.getResourceFileFromContext("files/TourEiffel.mp4"), "video/mp4");
    }

    public DocumentModel getEmptyVideoDocument(CoreSession session) {
        DocumentModel doc = session.createDocumentModel("/", "Video", "Video");
        return session.createDocument(doc);
    }

    public DocumentModel getVideoDocument(CoreSession session) {
        DocumentModel doc = session.createDocumentModel("/", "Video", "Video");

        doc.setPropertyValue("file:content", (Serializable) getVideoBlob());

        Map<String, Serializable> videoInfo = new HashMap<>();
        videoInfo.put(DURATION, 120.0d);
        videoInfo.put(WIDTH, 640L);
        videoInfo.put(HEIGHT, 360L);
        videoInfo.put(FRAME_RATE, 25.0d);
        videoInfo.put(FORMAT, "mp4");
        videoInfo.put(STREAMS, new ArrayList<>());

        doc.setPropertyValue("vid:info", (Serializable) videoInfo);

        return session.createDocument(doc);
    }

}
