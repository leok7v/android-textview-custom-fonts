Android TextView Custom Fonts
=============================

Small sample that allows to use custom font in layout.xml and style.xml for Android TextView.
Implementation holds all loaded fonts in memory relative to Application context.

If it is desired to hold only limited number of fonts "loaded"
  cache = new LruCache<String, Typeface>(MAXIMUM_NUMBER_OF_LOADED_FONTS)
can be used instead of HashMap in CustomFontFactory.

If it is desired to cache loaded fonts on Activity rather than Application context
the App shared data member:
    private CustomFontFactory factory;
can be easily moved to BaseActivity but beware in such scenario multiple Activities
using the same custom font will load it twice and will unload on garbage collection
of activity.

More sophisticated schemes of shared font cache management can be implemented but
it is outside of the scope of this sample.

This sample code is tested on Android API 11 to 17 and guaranteed not to work for
API < 11 (Honeycomb 3.0) see: http://en.wikipedia.org/wiki/Android_version_history

Free fonts used in sample assets are
http://www.fontsaddict.com/font/baroque-script.html
http://www.fontsaddict.com/font/curvaceous-script-regular.html
http://www.fontsaddict.com/font/dagerotypos.html

If you have any questions/suggestions/improvements feel free to contact me at

Leo.Kuznetsov@gmail.com

