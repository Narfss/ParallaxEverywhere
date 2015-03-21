
# ![](https://raw.githubusercontent.com/Narfss/ParallaxEverywhere/master/sample/src/main/res/drawable-mdpi/ic_launcher.png) Parallax Everywhere#

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ParallaxEverywhere-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1213)


Parallax everywhere (PEW) is a library with alternative android views using parallax effects.

## Demo ##
![](https://raw.githubusercontent.com/Narfss/ParallaxEverywhere/master/parallax-everywhere-animation-optimize.gif)

You can try the demo app on google play.

https://play.google.com/store/apps/details?id=com.fmsirvent.ParallaxEverywhereSample

### Views with effect? ###

Android view | PEW view
--- | ---
ImageView | PEWImageView
TextView | PEWTextView

### How it works? ###

* Any parallax views (PEW*) needs to be inside a view with scroll events, ej: scrollView, listView, gridView....
* Parallax effect on views will be related to its position on device screen.
* Parallax effect in ImageView is calculated with left image in Scale mode centerCrop, centerInside or center. You can't make more parallax effect.
* Parallax effect in no image views needs a size parallax parameter (read:  Attributes)

### Show me the code ###

Gradle dependencies:

```groovy
compile 'com.fmsirvent:parallaxeverywhere:1.0.4'
```

Code in layout:

```xml
  <!-- add on top parent layout: xmlns:pew="http://schemas.android.com/apk/res-auto" -->


   <FrameLayout
        android:layout_width="0dp"
        android:layout_height="match_parent"
        android:layout_gravity="center"
        android:layout_margin="10dp"
        android:layout_weight="1">

        <com.fmsirvent.ParallaxEverywhere.PEWImageView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_gravity="center"
            android:layout_margin="10dp"
            android:scaleType="centerCrop"
            android:src="@drawable/alicante_explanada" />

        <com.fmsirvent.ParallaxEverywhere.PEWTextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_margin="10dp"
            android:gravity="bottom|center_horizontal"
            android:text="@string/alicante_explanada"
            android:textColor="@android:color/white"
            pew:block_parallax_x="true"
            pew:parallax_x="160dp"
            pew:parallax_y="160dp"
            pew:reverse="reverseY" />

    </FrameLayout>

```

Proguard:

```
-dontwarn com.fmsirvent.ParallaxEverywhere.**
```

### Attributes ###

**All PEW**

* **reverse**  = ["none", "reverseX", "reverseY", "reverseBoth"]
  Change the direction of parallax effect. Default value "none".

* **block_parallax_x** and **block_parallax_y**  = "boolean"
  Blocks parallax effect. Default value false.

* **interpolation** = ["linear", "accelerate_decelerate", "accelerate", "anticipate", "anticipate_overshoot", "bounce", "decelerate", "overshoot"]
  Animation interpolation. Default value "linear".

* **update_onDraw**  = = "boolean"
  Experimental attribute: update the parallax effect on draw event. Try if the parents don't has scroll. Now only works on +API:16 (Jelly bean). Default value false.

**Only: no image PEW**

* **parallax_x** and **parallax_y** = "dimension"
In non widgets images is necessary specify the size of parallax effect. The size will be split in half for each side. Default value 0.

## License

ParallaxEverywhere is available under the MIT license. See the LICENSE file for more info.
