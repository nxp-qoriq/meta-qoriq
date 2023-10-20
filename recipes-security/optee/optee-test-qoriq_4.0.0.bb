# Copyright 2022-2023 NXP
require optee-test-qoriq.inc

OPTEE_TEST_BRANCH = "lf_4.0.y"
SRCREV = "${AUTOREV}"

PV:append = "+git${SRCPV}"
