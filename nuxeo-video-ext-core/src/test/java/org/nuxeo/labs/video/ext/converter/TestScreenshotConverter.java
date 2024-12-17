package org.nuxeo.labs.video.ext.converter;

import static org.nuxeo.ecm.platform.video.convert.Constants.POSITION_PARAMETER;
import static org.nuxeo.labs.video.ext.service.VideoStoryBoardServiceImpl.SCREENSHOT_CONVERTER_NAME;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.blobholder.BlobHolder;
import org.nuxeo.ecm.core.api.blobholder.SimpleBlobHolder;
import org.nuxeo.ecm.core.convert.api.ConversionService;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestScreenshotConverter {

    @Inject
    protected ConversionService conversionService;

    @Inject
    VideoExtFeature videoExtFeature;

    @Test
    public void testConverterIsAvailable() {
        Assert.assertTrue(conversionService.getRegistredConverters().contains(SCREENSHOT_CONVERTER_NAME));
        Assert.assertTrue(conversionService.isConverterAvailable(SCREENSHOT_CONVERTER_NAME).isAvailable());
    }

    @Test
    public void testConverter() {
        Blob blob = videoExtFeature.getVideoBlob();
        Map<String, Serializable> parameters = new HashMap<>();
        parameters.put(POSITION_PARAMETER, String.format("%dms", 500));
        BlobHolder bh = conversionService.convert(SCREENSHOT_CONVERTER_NAME, new SimpleBlobHolder(blob), parameters);
        Assert.assertNotNull(bh);
        Blob screenshot = bh.getBlob();
        Assert.assertNotNull(screenshot);
    }
}
