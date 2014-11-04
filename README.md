# Parallax Everywhere #

Parallax everywhere (PEW) is a library with alternative android widgets with parallax effects.

### Widgets width efect? ###

ImageView -> PEWImageView

TextView -> PEWTextView

### How it works? ###

* Any parallax widget (PEW*) needs be inside a view with scroll events, ej: scrollView, listView, gridView....
* Parallax effect on widgets will be related to its position on device screen.
* Parallax effect in ImageView is calculate witn left image in Scale mode centerCrop, centerInside or center. You can't make more parallax effect.
* Parallax effect in no image widgets needs a size parallax parameter (read:  Attributes)

### Attributes ###

**All PEW**

* reverse  = ["none", "reverseX", "reverseY", "reverseBoth"]
  Change animation direction of parallax effect

* "block_parallax_x" and "block_parallax_y"  = "boolean"
  Blocks parallax effect.

**Only: no image PEW* **

* "parallax_x" and "parallax_y" = "dimension"
In widgets widthout images is necessary specify the size of parallax effect. The size will be split in half for each side.
  

### Example ###

```
#!java

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

![parallax-everywhere-animation-optimize.gif](https://bitbucket.org/repo/4n5roa/images/3116732635-parallax-everywhere-animation-optimize.gif)

### How do I get set up? ###

#### .AAR on Android studio ####
* Download library.aar
* File -> New Module -> Import .JAT or .AAR Package
* File name: browse to library.aar
* Name project: ParallaxEverywhere

#### Gradle ####
*Comming soon*

```
#!gradle

  compile 'com.fmsirvent.parallaxeverywhere:1.0'

```