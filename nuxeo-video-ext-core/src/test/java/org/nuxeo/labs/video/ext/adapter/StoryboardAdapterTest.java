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

package org.nuxeo.labs.video.ext.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.StringBlob;
import org.nuxeo.labs.video.ext.api.Frame;
import org.nuxeo.labs.video.ext.api.Storyboard;
import org.nuxeo.labs.video.ext.utils.VideoExtFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features(VideoExtFeature.class)
public class StoryboardAdapterTest {

    @Inject
    CoreSession session;

    @Test
    public void testGetAdapterWithVideoDoc() {
        DocumentModel doc = session.createDocumentModel("/", "Video", "Video");
        doc = session.createDocument(doc);
        Storyboard adapter = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertNotNull(adapter);
    }

    @Test
    public void testGetAdapterWithFileDoc() {
        DocumentModel doc = session.createDocumentModel("/", "File", "File");
        doc = session.createDocument(doc);
        Storyboard adapter = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertNull(adapter);
    }

    @Test
    public void testGetAdapterUpdateOneFrame() {
        DocumentModel doc = session.createDocumentModel("/", "Video", "Video");
        doc = session.createDocument(doc);

        Storyboard adapter = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(0,adapter.size());
        Frame frame = new Frame(new StringBlob(""),1.0,"test");
        adapter.addFrame(frame);

        doc = session.saveDocument(doc);
        adapter = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(1,adapter.size());
        Frame savedFrame = adapter.getFrames().get(0);
        Assert.assertEquals(frame.getTimeInSeconds(),savedFrame.getTimeInSeconds(),0.001);
        Assert.assertEquals(frame.getComment(),savedFrame.getComment());
        Assert.assertEquals(frame.getBlob().getFilename(),savedFrame.getBlob().getFilename());
    }

    @Test
    public void testGetAdapterUpdateSeveralFrame() {
        DocumentModel doc = session.createDocumentModel("/", "Video", "Video");
        doc = session.createDocument(doc);

        Storyboard adapter = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(0,adapter.size());
        List<Frame> frames = new ArrayList<>();
        frames.add(new Frame(new StringBlob(""),1.0,"test"));
        frames.add(new Frame(new StringBlob(""),2.0,"test2"));
        adapter.addAllFrames(frames);

        doc = session.saveDocument(doc);
        adapter = doc.getAdapter(StoryboardAdapter.class);
        Assert.assertEquals(2,adapter.size());
    }

}
