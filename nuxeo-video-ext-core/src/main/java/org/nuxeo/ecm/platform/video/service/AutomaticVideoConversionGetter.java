/*
 * (C) Copyright 2023 Nuxeo (http://nuxeo.com/) and others.
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

package org.nuxeo.ecm.platform.video.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.nuxeo.runtime.api.Framework;

public class AutomaticVideoConversionGetter {

    public static Collection<VideoConversion> getAutomaticVideoConversions() {
        VideoService videoService = Framework.getService(VideoService.class);

        if (videoService instanceof VideoServiceImpl) {
            VideoServiceImpl videoServiceImpl = (VideoServiceImpl) videoService;
            return List.of(
                    videoServiceImpl.getVideoConversion("WEBM 480p"),
                    videoServiceImpl.getVideoConversion("MP4 480p")
            );
        } else {
            return Collections.emptyList();
        }
    }

    public static List<String> filerUpscalingConversion(long maxHeight, List<String> conversions) {
        VideoService videoService = Framework.getService(VideoService.class);
        return videoService.getAvailableVideoConversions()
                           .stream()
                           .filter(conversion -> conversion.getHeight() <= maxHeight)
                           .map(VideoConversion::getName)
                           .filter(conversions::contains)
                           .toList();
    }

}
