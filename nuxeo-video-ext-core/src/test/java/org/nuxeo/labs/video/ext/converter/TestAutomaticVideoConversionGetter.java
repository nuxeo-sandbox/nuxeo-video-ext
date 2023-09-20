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

package org.nuxeo.labs.video.ext.converter;

import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.platform.video.service.AutomaticVideoConversion;
import org.nuxeo.ecm.platform.video.service.AutomaticVideoConversionGetter;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestAutomaticVideoConversionGetter {

    @Test
    public void testGetAutomaticVideoConversions() {
        Collection<AutomaticVideoConversion> conversions = AutomaticVideoConversionGetter.getAutomaticVideoConversions();
        Assert.assertEquals(2, conversions.size());
    }

    @Test
    public void testFilterUpscaling() {
        List<String> conversions = AutomaticVideoConversionGetter.getAutomaticVideoConversions().stream().map(AutomaticVideoConversion::getName).toList();
        conversions = AutomaticVideoConversionGetter.filerUpscalingConversion(100L,conversions);
        Assert.assertEquals(0, conversions.size());
    }

    @Test
    public void testNoUpscaling() {
        List<String> conversions = AutomaticVideoConversionGetter.getAutomaticVideoConversions().stream().map(AutomaticVideoConversion::getName).toList();
        conversions = AutomaticVideoConversionGetter.filerUpscalingConversion(1080L,conversions);
        Assert.assertEquals(2, conversions.size());
    }

}
