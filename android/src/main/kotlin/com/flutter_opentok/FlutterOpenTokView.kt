package com.flutter_opentok

import android.content.Context
import android.graphics.Color
import android.opengl.GLSurfaceView
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.opentok.android.AudioDeviceManager
import com.opentok.android.BaseAudioDevice
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView
import kotlinx.serialization.json.Json


class FlutterOpenTokView(
    override var context: Context,
    binaryMessenger: BinaryMessenger,
    viewId: Int,
    args: Any?
    ) : PlatformView, MethodChannel.MethodCallHandler, VoIPProviderDelegate {

    val openTokView: FrameLayout
    private var provider: VoIPProvider? = null
    private var publisherSettings: PublisherSettings? = null
    private var switchedToSpeaker: Boolean = true
    private var channel: MethodChannel
    private var screenHeight: Int = LinearLayout.LayoutParams.MATCH_PARENT
    private var screenWidth: Int = LinearLayout.LayoutParams.MATCH_PARENT
    init {
        val channelName = "plugins.indoor.solutions/opentok_$viewId"
        channel = MethodChannel(binaryMessenger, channelName)
        val arguments: Map<*, *>? = args as? Map<*, *>
        openTokView = FrameLayout(context)
        openTokView.layoutParams = LinearLayout.LayoutParams(screenWidth, screenHeight)
        openTokView.setBackgroundColor(Color.TRANSPARENT)

        val publisherArg = arguments?.get("publisherSettings") as? String
        try {
            publisherSettings = publisherArg?.let { Json.decodeFromString(PublisherSettings.serializer(), it) }
        } catch (e: Exception) {
            if (FlutterOpentokPlugin.loggingEnabled) {
                print("OpenTok publisher settings error: ${e.message}")
            }
        }

        if (FlutterOpentokPlugin.loggingEnabled) {
            print("[FlutterOpenTokViewController] initialized")
        }

    }

    fun setup() {
        // Create VoIP provider
        createProvider()

        // Listen for method calls from Dart.
        channel.setMethodCallHandler(this)
    }

    override fun getView(): View {
        return openTokView
    }

    override fun dispose() {

    }

    private fun configureAudioSession() {
        if (FlutterOpentokPlugin.loggingEnabled) {
            print("[FlutterOpenTokViewController] Configure audio session")
            print("[FlutterOpenTokViewController] Switched to speaker = $switchedToSpeaker")
        }

        if (switchedToSpeaker) {
            AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.SpeakerPhone);
        } else {
            AudioDeviceManager.getAudioDevice().setOutputMode(BaseAudioDevice.OutputMode.Handset);
        }
    }

    // Convenience getter for current video view based on provider implementation
    private val subscriberView: View?
        get() {
            if (provider is OpenTokVoIPImpl)
                return (provider as OpenTokVoIPImpl).subscriberView
            return null
        }

    private val publisherView: View?
        get() {
            if (provider is OpenTokVoIPImpl)
                return (provider as OpenTokVoIPImpl).publisherView
            return null
        }

    /** Create an instance of VoIPProvider. This is what implements VoIP for the application.*/
    private fun createProvider() {
        provider = OpenTokVoIPImpl(delegate = this, publisherSettings = publisherSettings)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "create") {
            if (call.arguments == null) return

            val methodArgs = call.arguments as? Map<String, Any>
            val apiKey = methodArgs?.get("apiKey") as? String
            val sessionId = methodArgs?.get("sessionId") as? String
            val token = methodArgs?.get("token") as? String

            if (apiKey != null && sessionId != null && token != null) {
                provider?.connect(apiKey, sessionId, token)
                result.success(null)
            } else {
                result.error("CREATE_ERROR", "Android could not extract flutter arguments in method: (create)","")
            }
        } else if (call.method == "destroy") {
            provider?.disconnect()
            result.success(null)
        } else if (call.method == "enablePublisherVideo") {
            provider?.enablePublisherVideo()
            result.success(null)
        } else if (call.method == "disablePublisherVideo") {
            provider?.disablePublisherVideo()
            result.success(null)
        } else if (call.method == "unmutePublisherAudio") {
            provider?.unmutePublisherAudio()
            result.success(null)
        } else if (call.method == "mutePublisherAudio") {
            provider?.mutePublisherAudio()
            result.success(null)
        } else if (call.method == "muteSubscriberAudio") {
            provider?.muteSubscriberAudio()
            result.success(null)
        } else if (call.method == "unmuteSubscriberAudio") {
            provider?.unmuteSubscriberAudio()
            result.success(null)
        } else if (call.method == "switchAudioToSpeaker") {
            switchedToSpeaker = true
            configureAudioSession()
            result.success(null)
        } else if (call.method == "switchAudioToReceiver") {
            switchedToSpeaker = false
            configureAudioSession()
            result.success(null)
        } else if (call.method == "getSdkVersion") {
            result.success("1")
        } else if (call.method == "switchCamera") {
            provider?.switchCamera()
            result.success(null)
        } else {
            result.notImplemented()
        }
    }

    private fun channelInvokeMethod(method: String, arguments: Any?) {
        channel.invokeMethod(method, arguments)
    }


    private fun addPublisherVideo() {
        openTokView.addView(publisherView!!)

        val layoutDimension = openTokView.width / 3;
        val layout = FrameLayout.LayoutParams(layoutDimension, layoutDimension + (layoutDimension / 3), Gravity.BOTTOM or Gravity.RIGHT)
        layout.setMargins(16,16,16, 16)
        publisherView!!.layoutParams = layout
        publisherView!!.bringToFront()

        publisherView!!.setOnTouchListener(object : View.OnTouchListener {
            var dX: Float = 0F
            var dY: Float = 0F

            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view!!.x - event.rawX
                        dY = view.y - event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        var newX = event.rawX + dX
                        if (newX < 0)
                            newX = 0F
                        if (newX > openTokView.width - view!!.width)
                            newX = (openTokView.width - view.width).toFloat()

                        var newY = event.rawY + dY
                        if (newY < 0)
                            newY = 0F
                        if (newY > openTokView.height - view.height)
                            newY = (openTokView.height - view.height).toFloat()

                        view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start()

                        openTokView.invalidate()

                    }
                    else -> return false
                }
                return true
            }
        })


        if (publisherView is GLSurfaceView) {
            (publisherView as GLSurfaceView).setZOrderOnTop(true)
        }
    }

    override fun willConnect() {
        channelInvokeMethod("onWillConnect", null)
    }

    override fun didConnect() {
        configureAudioSession()

        if (provider?.isAudioOnly == false && publisherView != null) {
            addPublisherVideo()
        }

        channelInvokeMethod("onSessionConnect", null)
    }

    override fun didDisconnect() {
        channelInvokeMethod("onSessionDisconnect", null)
    }

    override fun didReceiveVideo() {
        if (FlutterOpentokPlugin.loggingEnabled) {
            print("[FlutterOpenTokView] Receive video")
        }

        if (subscriberView != null) {
            openTokView.addView(subscriberView!!)
            subscriberView!!.isEnabled = false
            if (subscriberView is GLSurfaceView) {
                (subscriberView as GLSurfaceView).setZOrderOnTop(false)
            }
        }

        channelInvokeMethod("onReceiveVideo", null)
    }

    override fun didCreateStream() {
        channelInvokeMethod("onCreateStream",null)
    }

    override fun didCreatePublisherStream() {
        channelInvokeMethod("onCreatePublisherStream", null)
    }

    override fun didDropStream() {
        channelInvokeMethod("onDroppedStream", null)

        if(subscriberView != null) {
            openTokView.removeView(subscriberView);
            provider?.subscriber = null;
        }
    }
}