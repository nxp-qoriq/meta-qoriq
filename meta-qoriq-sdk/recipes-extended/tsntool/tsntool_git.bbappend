FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " file://0001-Fix-the-function-prototype-doesn-t-match-the-impleme.patch"
SRCREV = "5b13e8858beee5268cf782b9f35743858b55c98b"
CFLAGS += " -Wno-error=incompatible-pointer-types -Wno-error=implicit-function-declaration -Wno-error=int-conversion"
