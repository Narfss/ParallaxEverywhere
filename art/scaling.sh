#Script by: fmSrivent http://www.fmSirvent.com

rm -dR publish
mkdir publish

#ic_launcher_web
inkscape --export-width=512 --export-height=512 ic_launcher.svg --export-png=publish/ic_launcher.png

#feature_graphic
inkscape feature_graphic.svg --export-png=publish/feature_graphic.png

rm -d ../sample/src/main/res/drawable-mdpi/*
rm -d ../sample/src/main/res/drawable-hdpi/*
rm -d ../sample/src/main/res/drawable-xhdpi/*
rm -d ../sample/src/main/res/drawable-xxhdpi/*
rm -d ../sample/src/main/res/drawable-xxxhdpi/*


mkdir -p ../sample/src/main/res/drawable-mdpi
mkdir -p ../sample/src/main/res/drawable-hdpi
mkdir -p ../sample/src/main/res/drawable-xhdpi
mkdir -p ../sample/src/main/res/drawable-xxhdpi
mkdir -p ../sample/src/main/res/drawable-xxxhdpi

#ic_launcher
inkscape --export-width=192 --export-height=192 ic_launcher.svg --export-png=../sample/src/main/res/drawable-xxxhdpi/ic_launcher.png
inkscape --export-width=144 --export-height=144 ic_launcher.svg --export-png=../sample/src/main/res/drawable-xxhdpi/ic_launcher.png
inkscape --export-width=96 --export-height=96 ic_launcher.svg --export-png=../sample/src/main/res/drawable-xhdpi/ic_launcher.png
inkscape --export-width=72 --export-height=72 ic_launcher.svg --export-png=../sample/src/main/res/drawable-hdpi/ic_launcher.png
inkscape --export-width=48 --export-height=48 ic_launcher.svg --export-png=../sample/src/main/res/drawable-mdpi/ic_launcher.png


#convert resize
#convert -resize 75% ic_launcher.svg drawable-xxhdpi/ic_launcher.png
#convert -resize 50% ic_launcher.svg drawable-xhdpi/ic_launcher.png
#convert -resize 37.5% ic_launcher.svg drawable-hdpi/ic_launcher.png
#convert -resize 25% ic_launcher.svg drawable-mdpi/ic_launcher.png


# resized data
cd xhdpi_resize
cp * ../../sample/src/main/res/drawable-xhdpi
mogrify -path ../../sample/src/main/res/drawable-hdpi/ -format png -resize 75% *.png
mogrify -path ../../sample/src/main/res/drawable-mdpi/ -format png -resize 50% *.png
