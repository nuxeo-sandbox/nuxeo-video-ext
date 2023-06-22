package org.nuxeo.labs.video.ext.converter;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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

import static org.nuxeo.labs.video.ext.automation.functions.VideoFrameFunctions.IMAGE_MEAN_GRAY_CONVERTER;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class TestImageMeanGrayInfoConverter {

    @Inject
    protected ConversionService conversionService;

    @Inject
    VideoExtFeature videoExtFeature;

    @Test
    public void testConverterIsAvailable() {
        Assert.assertTrue(conversionService.getRegistredConverters().contains(IMAGE_MEAN_GRAY_CONVERTER));
        Assert.assertTrue(conversionService.isConverterAvailable(IMAGE_MEAN_GRAY_CONVERTER).isAvailable());
    }

    @Test
    public void testConverterWithBlackFrame() throws IOException {
        Blob blob = videoExtFeature.getBlackFrameBlob();
        Map<String, Serializable> parameters = new HashMap<>();
        BlobHolder bh = conversionService.convert(IMAGE_MEAN_GRAY_CONVERTER, new SimpleBlobHolder(blob), parameters);
        Assert.assertNotNull(bh);
        Blob info = bh.getBlob();
        Assert.assertNotNull(info);
        double mean = Double.parseDouble(info.getString());
        Assert.assertEquals(0,mean,0.1);
    }
}
