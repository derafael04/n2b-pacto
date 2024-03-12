// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'flutter_opentok.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

OpenTokConfiguration _$OpenTokConfigurationFromJson(
        Map<String, dynamic> json) =>
    OpenTokConfiguration(
      token: json['token'] as String?,
      apiKey: json['apiKey'] as String?,
      sessionId: json['sessionId'] as String?,
    );

Map<String, dynamic> _$OpenTokConfigurationToJson(
        OpenTokConfiguration instance) =>
    <String, dynamic>{
      'token': instance.token,
      'apiKey': instance.apiKey,
      'sessionId': instance.sessionId,
    };

OTPublisherKitSettings _$OTPublisherKitSettingsFromJson(
        Map<String, dynamic> json) =>
    OTPublisherKitSettings(
      name: json['name'] as String?,
      audioTrack: json['audioTrack'] as bool?,
      videoTrack: json['videoTrack'] as bool?,
      videoInitialized: json['videoInitialized'] as bool?,
      audioBitrate: json['audioBitrate'] as int?,
      cameraResolution: $enumDecodeNullable(
          _$OTCameraCaptureResolutionEnumMap, json['cameraResolution']),
      cameraFrameRate: $enumDecodeNullable(
          _$OTCameraCaptureFrameRateEnumMap, json['cameraFrameRate']),
    );

Map<String, dynamic> _$OTPublisherKitSettingsToJson(
        OTPublisherKitSettings instance) =>
    <String, dynamic>{
      'name': instance.name,
      'audioTrack': instance.audioTrack,
      'videoTrack': instance.videoTrack,
      'videoInitialized': instance.videoInitialized,
      'audioBitrate': instance.audioBitrate,
      'cameraResolution':
          _$OTCameraCaptureResolutionEnumMap[instance.cameraResolution],
      'cameraFrameRate':
          _$OTCameraCaptureFrameRateEnumMap[instance.cameraFrameRate],
    };

const _$OTCameraCaptureResolutionEnumMap = {
  OTCameraCaptureResolution.OTCameraCaptureResolutionLow:
      'OTCameraCaptureResolutionLow',
  OTCameraCaptureResolution.OTCameraCaptureResolutionMedium:
      'OTCameraCaptureResolutionMedium',
  OTCameraCaptureResolution.OTCameraCaptureResolutionHigh:
      'OTCameraCaptureResolutionHigh',
};

const _$OTCameraCaptureFrameRateEnumMap = {
  OTCameraCaptureFrameRate.OTCameraCaptureFrameRate30FPS:
      'OTCameraCaptureFrameRate30FPS',
  OTCameraCaptureFrameRate.OTCameraCaptureFrameRate15FPS:
      'OTCameraCaptureFrameRate15FPS',
  OTCameraCaptureFrameRate.OTCameraCaptureFrameRate7FPS:
      'OTCameraCaptureFrameRate7FPS',
  OTCameraCaptureFrameRate.OTCameraCaptureFrameRate1FPS:
      'OTCameraCaptureFrameRate1FPS',
};
