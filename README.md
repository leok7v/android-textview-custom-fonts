Android TextView Custom Fonts
=============================

Small sample that allows to use custom font in layout.xml and style.xml for Android TextView.
Implementation holds all loaded fonts in memory relative to Application context.

If it is desired to hold only limited number of fonts "loaded"
  cache = new LruCache<String, Typeface>(MAXIMUM_NUMBER_OF_LOADED_FONTS)
can be used instead of HashMap in CustomFontFactory.

More sophisticated schemes of shared font cache management can be implemented but
it is outside of the scope of this sample.

CustomFontFactory is a shared singleton that is used in BaseActivity.
It does not have any provisions for thread safety thus it must be used on application main thread.

This sample code is tested on Android API 12 (Honeycomb 3.1) to 19 and guaranteed not to work for
API <= 11 (Honeycomb 3.0) see: http://en.wikipedia.org/wiki/Android_version_history

Free fonts used in sample assets are
http://www.fontsaddict.com/font/baroque-script.html
http://www.fontsaddict.com/font/curvaceous-script-regular.html
http://www.fontsaddict.com/font/dagerotypos.html

If you have any questions/suggestions/improvements feel free to contact me at

Leo.Kuznetsov@gmail.com

![alt tag](https://raw.github.com/leok7v/android-textview-custom-fonts/master/screenshot.png)
