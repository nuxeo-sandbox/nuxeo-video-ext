# Description

This plugin provides an extended API to handle video files which is less monolithic and more extensible that the
default platform implementation.

# How to build

```bash
git clone https://github.com/nuxeo-sandbox/nuxeo-video-ext
cd nuxeo-video-ext
mvn clean install
```

# Configuration

Several configuration settings are available.

## nuxeo.conf

| Property name                  | description                                                                     |
|--------------------------------|:--------------------------------------------------------------------------------|
| nuxeo.video.default.info       | A boolean property to enable/disable the default video metadata mapping         |
| nuxeo.video.default.conversion | A boolean property to enable/disable the default video conversions              |
| nuxeo.video.default.storyboard | A boolean property to enable/disable the default video storyboard and thumbnail |

# Plugin Features

## Services

This plugin brakes down the video related features into independent services that can be individually overridden:

- VideoInfoService provides an API to extract and map video file metadata
- VideoStoryboardService provides an API to build image storyboards from video files

## Event and Listeners

By default, metadata extraction, conversions and storyboard generation happen asynchronously and are orchestrated using
events and listeners.

- [videoChangedListener](https://github.com/nuxeo-sandbox/nuxeo-video-ext/blob/45347a803868171e8c0134ed32fa199fd6a3c909/nuxeo-video-ext-core/src/main/resources/OSGI-INF/listener-contrib.xml#L7)
  listens to the `documentCreated` and `documentChanged` events and if the video file changed, schedules a
  ExtendedVideoInfoWork to extract video metadata. Once completed, the work will fire a `videoInfoDone` event
- [triggerVideoConversionlistener](https://github.com/nuxeo-sandbox/nuxeo-video-ext/blob/45347a803868171e8c0134ed32fa199fd6a3c909/nuxeo-video-ext-core/src/main/resources/OSGI-INF/listener-contrib.xml#L13)
  and [triggerVideoStoryboardlistener](https://github.com/nuxeo-sandbox/nuxeo-video-ext/blob/45347a803868171e8c0134ed32fa199fd6a3c909/nuxeo-video-ext-core/src/main/resources/OSGI-INF/listener-contrib.xml#L18)
  listens to the `videoInfoDone` event, then respectively schedule a VideoConversionWork and
  ExtendedVideoStoryboardWork. These works will fire `videoConversionsDone` abd `videoStoryboardDone` events once
  completed.

## Automation

Several Automation operations are made available by this plugin.

### Video Storyboard

```bash
curl 'http://localhost:8080/nuxeo/api/v1/automation/Video.Storyboard' \
  -H 'Content-Type: application/json' \
  -H 'properties: *' \
  --data-raw '{"params":{"save":true},"context":{},"input":"<DOC_UUID>"}' \
  --compressed
```

Parameters:

| Name                  | Description                                                                               | Type         | Required | Default value |
|:----------------------|:------------------------------------------------------------------------------------------|:-------------|:---------|:--------------|
| timecodeListInSeconds | A list of time codes in seconds corresponding to each frame of the storyboard to generate | List<Double> | false    | empty         |
| save                  | Save the document                                                                         | boolean      | false    | false         |

### Video Preview

```bash
curl 'http://localhost:8080/nuxeo/api/v1/automation/Video.Preview' \
  -H 'Content-Type: application/json' \
  -H 'properties: *' \
  --data-raw '{"params":{"save":true},"context":{},"input":"<DOC_UUID>"}'
```

Parameters:

| Name             | Description                                                                         | Type    | Required | Default value |
|:-----------------|:------------------------------------------------------------------------------------|:--------|:---------|:--------------|
| timecodeInSecond | The time code of the frame to extract and use as the playback preview and thumbnail | double  | false    |               |
| save             | Save the document                                                                   | boolean | false    | false         |


# Custom configuration samples
## Custom storyboard configuration with Nuxeo Studio

When using Nuxeo AI and the AWS Rekognition video segment feature, you can leverage the results to generate a more compelling storyboard than by using the default fixed time configuration.
To do that you need to first:
- set `nuxeo.video.default.storyboard=false` in nuxeo.conf to disable the default storyboard
- create an event handler in Nuxeo Studio to listen to the `ENRICHMENT_MODIFIED` event and configure the following automation script

```js
function run(input, params) {

  var enrichmentName = ctx.Event.getContext().getProperties().comment;

  if (enrichmentName === 'aws.videoSegmentDetection') {

    var enrichmentItems =  toJsArray(input['enrichment:items']);

    var segments;
    
    enrichmentItems.forEach(function(item) {
      if (item.model === 'aws.videoSegmentDetection') {
        segments = item;
      }
    });
       
    var timecodeInSeconds = toJsArray(segments.suggestions[0].labels).filter(function(segment) {
      return segment.label === 'SHOT';                 
    }).map(function(segment) {
      return segment.timestamp / 1000;
    });    
    
    input = Video.Storyboard(input, {
      'save': false,
      'timecodeListInSeconds': timecodeInSeconds
    });
    
    var previewTimecode = timecodeInSeconds.length > 0 ?  timecodeInSeconds[0] : 0;
       
    input = Video.Preview(input, {
      'save': true,
      'timecodeInSecond': previewTimecode
    });
 
  }
  
  return input;
}

function toJsArray(javaArray) {
  var jsArray = [];
  for(var i=0; i < javaArray.length; i++) {
    jsArray.push(javaArray[i]);
  }
  return jsArray;
}
```

# Support

**These features are not part of the Nuxeo Production platform.**

These solutions are provided for inspiration and we encourage customers to use them as code samples and learning
resources.

This is a moving project (no API maintenance, no deprecation process, etc.) If any of these solutions are found to be
useful for the Nuxeo Platform in general, they will be integrated directly into platform, not maintained here.

# Nuxeo Marketplace

This plugin is published on
the [marketplace](https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-vntana-connector)

# License

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

# About Nuxeo

Nuxeo Platform is an open source Content Services platform, written in Java. Data can be stored in both SQL & NoSQL
databases.

The development of the Nuxeo Platform is mostly done by Nuxeo employees with an open development model.

The source code, documentation, roadmap, issue tracker, testing, benchmarks are all public.

Typically, Nuxeo users build different types of information management solutions
for [document management](https://www.nuxeo.com/solutions/document-management/)
, [case management](https://www.nuxeo.com/solutions/case-management/),
and [digital asset management](https://www.nuxeo.com/solutions/dam-digital-asset-management/), use cases. It uses
schema-flexible metadata & content models that allows content to be repurposed to fulfill future use cases.

More information is available at [www.nuxeo.com](https://www.nuxeo.com).
