SRC_URI = "git://git.lttng.org/lttng-modules.git;branch=stable-2.10 \
           file://Makefile-Do-not-fail-if-CONFIG_TRACEPOINTS-is-not-en.patch \
           file://BUILD_RUNTIME_BUG_ON-vs-gcc7.patch \
"
SRCREV = "afbee416289de1de537ae0992e05ac14f85f69fd"

S = "${WORKDIR}/git"
