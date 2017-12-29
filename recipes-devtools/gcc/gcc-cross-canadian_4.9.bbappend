require gcc-fsl.inc

DEPENDS_append_qoriq-ppc = " nativesdk-isl nativesdk-cloog"
RDEPENDS_${PN}_append_qoriq-ppc = " nativesdk-isl nativesdk-cloog"

EXTRA_OECONF_append_qoriq-ppc = " --with-isl=${STAGING_DIR_HOST}${SDKPATHNATIVE}${prefix_nativesdk} \
                 --with-cloog=${STAGING_DIR_HOST}${SDKPATHNATIVE}${prefix_nativesdk} \
"

INSANE_SKIP_${PN} += "libdir file-rdeps build-deps"
INSANE_SKIP_${PN}-dbg += "libdir"
