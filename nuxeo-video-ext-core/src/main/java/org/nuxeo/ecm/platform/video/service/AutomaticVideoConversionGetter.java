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
import java.util.Comparator;
import java.util.stream.Collectors;

import org.nuxeo.runtime.api.Framework;

public class AutomaticVideoConversionGetter {

    public static Collection<AutomaticVideoConversion> getAutomaticVideoConversions() {
        VideoService videoService = Framework.getService(VideoService.class);

        if (videoService instanceof VideoServiceImpl) {
            AutomaticVideoConversionContributionHandler automaticVideoConversions = ((VideoServiceImpl) videoService).automaticVideoConversions;
            return automaticVideoConversions.registry.values()
                                                     .stream()
                                                     .filter(AutomaticVideoConversion::isEnabled)
                                                     .sorted(Comparator.comparingInt(
                                                             AutomaticVideoConversion::getOrder))
                                                     .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

}
