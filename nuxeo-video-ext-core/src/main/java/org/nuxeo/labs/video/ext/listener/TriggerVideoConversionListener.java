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

import static org.nuxeo.ecm.core.api.security.SecurityConstants.SYSTEM_USERNAME;
import static org.nuxeo.ecm.platform.video.computation.RecomputeTranscodedVideosComputation.PARAM_CONVERSION_NAMES;
import static org.nuxeo.ecm.platform.video.computation.RecomputeTranscodedVideosComputation.PARAM_XPATH;
import static org.nuxeo.ecm.platform.video.computation.RecomputeVideoInfoComputation.RECOMPUTE_ALL_VIDEO_INFO;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.bulk.BulkService;
import org.nuxeo.ecm.core.bulk.message.BulkCommand;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.ecm.platform.video.VideoDocument;
import org.nuxeo.ecm.platform.video.VideoInfo;
import org.nuxeo.ecm.platform.video.action.RecomputeVideoConversionsAction;
import org.nuxeo.ecm.platform.video.service.AutomaticVideoConversion;
import org.nuxeo.ecm.platform.video.service.AutomaticVideoConversionGetter;
import org.nuxeo.runtime.api.Framework;

public class TriggerVideoConversionListener implements EventListener {

    public static final String VIDEOS_QUERY = "SELECT * FROM Document WHERE ecm:uuid = '%s'";

    @Override
    public void handleEvent(Event event) {
        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext docCtx)) {
            return;
        }

        DocumentModel doc = docCtx.getSourceDocument();


        List<String> conversions = AutomaticVideoConversionGetter.getAutomaticVideoConversions()
                                                                 .stream()
                                                                 .map(AutomaticVideoConversion::getName)
                                                                 .collect(Collectors.toList());

        if ("false".equals(Framework.getProperty("nuxeo.video.conversion.allow.upscaling","true"))) {
            VideoDocument video = doc.getAdapter(VideoDocument.class,true);
            VideoInfo videoInfo = video.getVideo().getVideoInfo();
            long shortDimension = Math.min(videoInfo.getWidth(), videoInfo.getHeight());
            conversions = AutomaticVideoConversionGetter.filerUpscalingConversion(shortDimension,conversions);
        }

        if (!conversions.isEmpty()) {
            BulkService bulkService = Framework.getService(BulkService.class);
            bulkService.submit(new BulkCommand.Builder(RecomputeVideoConversionsAction.ACTION_NAME,
                    String.format(VIDEOS_QUERY, doc.getId()), SYSTEM_USERNAME)
                    .repository(ctx.getCoreSession().getRepositoryName())
                    .param(PARAM_XPATH, "file:content")
                    .param(RECOMPUTE_ALL_VIDEO_INFO, false)
                    .param(PARAM_CONVERSION_NAMES, (Serializable) conversions)
                    .build());
        }
    }


}
