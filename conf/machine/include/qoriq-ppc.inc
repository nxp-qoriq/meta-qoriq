# Provides the common settings for QorIQ PPC

KERNEL_IMAGETYPE ?= "uImage"

SERIAL_CONSOLES ?= "115200;ttyS0 115200;ttyS1 115200;ttyEHV0"
SERIAL_CONSOLES_CHECK ?= "${SERIAL_CONSOLES}"

MACHINEOVERRIDES =. "qoriq-ppc:"

require conf/machine/include/qoriq-base.inc

GCCVERSION_qoriq-ppc = "4.9.2"
BINUVERSION_qoriq-ppc = "2.25"
GLIBCVERSION_qoriq-ppc = "2.20"
PREFERRED_VERSION_virtual/nativesdk-libiconv = "2.20"
