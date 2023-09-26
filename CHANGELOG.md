# Changelog

## 2023.3.0
- Add an operation to store a video conversion on a document

## 2023.2.0
- Add fallback event if no automatic conversion without upscaling is available
- Add nuxeo.conf parameter to prevent video upscaling
- Use the conversion height parameter as width for portrait video conversions

## 2023.1.0
- Previews are no longer computed in the storyboard operation

## 2023.0.0
- use bulk action to compute conversions
- Set correct filename of storyboard frames

## 2021.4.0
- Add Automation function to identify black frames
- Fix wrong timecode rounding in storyboard service

## 2021.3.0
- fix cast issues with timecode array containing a single integer value 

## 2021.2.0
- Add automation adapter for integer to double in order to fix issues when converting numbers from automation js
- All automation operation now takes double inputs for timecodes

## 2021.1.0
- Add video screenshot automation operation

## 2021.0.0
- Initial Release
