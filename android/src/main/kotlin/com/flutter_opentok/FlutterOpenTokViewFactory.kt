package com.flutter_opentok

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MessageCodec
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory


class FlutterOpenTokViewFactory(private var binaryMessenger: BinaryMessenger, private var appContext: Context) :
    PlatformViewFactory(StandardMessageCodec.INSTANCE) {

    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        val arguments: Map<*, *>? = args as? Map<*, *>
        if (arguments?.get("loggingEnabled") as Boolean) {
            FlutterOpentokPlugin.loggingEnabled = true
        }

        val view = FlutterOpenTokView(context!!, binaryMessenger, viewId, args)
        view.setup()

        return view
    }
}