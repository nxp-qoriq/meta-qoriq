# Copyright 2020 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Framebuffer (scriptable) image viewer"
DESCRIPTION = "FIM (Fbi IMproved) aims to be a highly customizable and scriptable \
               image viewer targeted at users who are comfortable with software \
               like the Vim text editor or the Mutt mail user agent."
SECTION = "utils"
HOMEPAGE = "http://www.autistici.org/dezperado/fim/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=fa01bff138cc98a62b8840a157951c88"

# flex with provide /usr/include/FlexLexer.h
DEPENDS = "flex-native bison-native flex"
RDEPENDS_${PN} += "bash"

SRC_URI = "http://download.savannah.nongnu.org/releases/fbi-improved/${BPN}-${PV}.tar.gz"
SRC_URI[sha256sum] = "f77f45d38cbd2ddb5a21ce48b6ecb44e7ddcf77d00635d7c7dbe74ea083b8b88"

PARALLEL_MAKE = ""

inherit autotools pkgconfig

# Don't use provided regex.c
EXTRA_OECONF = "LIBS=-lpthread fim_cv_regex=no fim_cv_regex_broken=no \
    --enable-framebuffer \
    --disable-djvu \
    --disable-ps \
    --disable-xcftopnm \
    --disable-convert \
    --disable-inkscape \
    --disable-xfig \
    --disable-dia \
    --disable-aa \
    --disable-sdl \
    --enable-custom-status-bar \
    --disable-hardcoded-font \
"

# Note: imlib2 is located in meta-efl layer.
PACKAGECONFIG ??= "jpeg rl"
PACKAGECONFIG[png] = "--enable-png,--disable-png,libpng"
PACKAGECONFIG[jpeg] = "--enable-jpeg,--disable-jpeg,jpeg"
PACKAGECONFIG[tiff] = "--enable-tiff,--disable-tiff,tiff"
PACKAGECONFIG[gif] = "--enable-gif,--disable-gif,giflib"
PACKAGECONFIG[poppler] = "--enable-poppler,--disable-poppler,poppler"
PACKAGECONFIG[pdf] = "--enable-pdf,--disable-pdf,"
PACKAGECONFIG[magick] = "--enable-graphicsmagick,--disable-graphicsmagick,imagemagick"
PACKAGECONFIG[imlib2] = "--enable-imlib2,--disable-imlib2,imlib2"
PACKAGECONFIG[rl] = "--enable-readline,--disable-readline,readline"
PACKAGECONFIG[exif] = "--enable-exif,--disable-exif,libexif"
PACKAGECONFIG[readline] = "--enable-readline,--disable-readline,readline"

