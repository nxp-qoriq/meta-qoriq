# Copyright 2022-2023 NXP
require optee-test-qoriq.inc

OPTEE_TEST_BRANCH = "lf-6.1.36_2.1.0"
SRCREV = "e0ebd5193070e0215b5389da191bc33f4f478222"

PV:append = "+git${SRCPV}"
