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

* reverse ["none", "reverseX", "reverseY", "reverseBoth"]
  Change animation direction of parallax effect

* "block_parallax_x" and "block_parallax_y" "boolean"
  Blocks parallax effect.

**Only: no image PEW* **

* "parallax_x" and "parallax_y" "dimension"
  


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

