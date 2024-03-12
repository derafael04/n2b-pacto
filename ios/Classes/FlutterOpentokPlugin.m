#import "FlutterOpentokPlugin.h"
#import <ondoc_flut_opentok/ondoc_flut_opentok-Swift.h>
#import "UserAgent.h"

@implementation FlutterOpentokPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterOpentokPlugin registerWithRegistrar:registrar];
}
@end
