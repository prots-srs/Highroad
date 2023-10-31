package com.protsprog.highroad.glance

/*
TO READ

https://developer.android.com/jetpack/compose/glance

https://github.com/android/platform-samples/blob/main/samples/user-interface/appwidgets/
 */
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class HighroadWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = HighroadWidget()
//    override val glanceAppWidget: GlanceAppWidget = ButtonsGlanceWidget()
}

class ToDoListWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ToDoListGlanceWidget()
    //    override val glanceAppWidget: GlanceAppWidget = ListGlanceWidget()
}